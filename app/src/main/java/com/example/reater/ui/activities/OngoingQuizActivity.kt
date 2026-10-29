package com.example.reater.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.adapters.QuestionIndexAdapter
import com.example.reater.databinding.ActivityOngoingQuizBinding
import com.example.reater.models.Quiz

class OngoingQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOngoingQuizBinding
    private lateinit var quiz: Quiz
    private val args:OngoingQuizActivityArgs by navArgs()
    private var currentQuestionIndex = 0
    private lateinit var timer: CountDownTimer
    private val qadapter by lazy {QuestionIndexAdapter()}
    private var backButtonPressed = false

    // Track user answers (Key: Question Index, Value: Selected Option String like "Option 1")
    private val userAnswers = mutableMapOf<Int, String>()
    private var isSubmitting = false
    private var isQuizStarted = false
    private var isInternalDialogShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PROCTORING: Prevent screenshots and screen recording
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )

        binding = ActivityOngoingQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extraQuiz = intent.getParcelableExtra<Quiz>("quiz")
        quiz = extraQuiz ?: try {
            args.quiz
        } catch (e: Exception) {
            Quiz()
        }

        if (!quiz.quizList.isNullOrEmpty()) {
            binding.question = quiz.quizList?.get(0)
        }
        
        showProctoringStartDialog()
    }

    private fun showProctoringStartDialog() {
        isInternalDialogShowing = true
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Proctoring Rules")
        builder.setMessage("This quiz is strictly proctored.\n\n• Do NOT minimize the app.\n• Do NOT switch windows or open notifications.\n• Screenshots are disabled.\n\nAny violation will forcefully submit your quiz. Click below when you are ready.")
        builder.setPositiveButton("Start Test") { _, _ ->
            isInternalDialogShowing = false
            isQuizStarted = true
            setupUi()
        }
        builder.setCancelable(false)
        builder.create().show()
    }

    private fun setupUi() {
        binding.qnoRV.adapter = qadapter
        binding.qnoRV.layoutManager = LinearLayoutManager(this)
        val durationMins = (quiz.quizDuration?.toIntOrNull() ?: 15).coerceAtLeast(1)
        binding.progressBar3.max = durationMins * 60000
        
        updateQuestionUI()

        binding.previousQuestion.setOnClickListener {
            if (currentQuestionIndex == 0) {
                Toast.makeText(this, "This is the first question only", Toast.LENGTH_SHORT).show()
            } else {
                saveCurrentAnswer()
                currentQuestionIndex--
                updateQuestionUI()
            }
        }

        binding.nextQuestion.setOnClickListener {
            saveCurrentAnswer()
            if (currentQuestionIndex == (quiz.quizList?.size ?: 0) - 1) {
                showSubmitConfirmDialog()
            } else {
                currentQuestionIndex++
                updateQuestionUI()
            }
        }

        startTimer()
    }

    private fun saveCurrentAnswer() {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        if (selectedId != -1) {
            val userAns = when (selectedId) {
                binding.radioButton1.id -> "Option 1"
                binding.radioButton2.id -> "Option 2"
                binding.radioButton3.id -> "Option 3"
                binding.radioButton4.id -> "Option 4"
                else -> ""
            }
            userAnswers[currentQuestionIndex] = userAns
        } else {
            // Remove entry if user cleared the choice (optional)
            userAnswers.remove(currentQuestionIndex)
        }
    }

    private fun updateQuestionUI() {
        binding.question = quiz.quizList?.get(currentQuestionIndex)
        binding.radioGroup.clearCheck() // Clear selected radio button naturally

        // Restore previous answer if they revisit the question
        val previousAnswer = userAnswers[currentQuestionIndex]
        if (previousAnswer != null) {
            when (previousAnswer) {
                "Option 1" -> binding.radioButton1.isChecked = true
                "Option 2" -> binding.radioButton2.isChecked = true
                "Option 3" -> binding.radioButton3.isChecked = true
                "Option 4" -> binding.radioButton4.isChecked = true
            }
        }
        
        if (currentQuestionIndex == (quiz.quizList?.size ?: 0) - 1) {
            binding.nextQuestion.text = "Submit Quiz"
        } else {
            binding.nextQuestion.text = "Next Question"
        }
    }

    private fun showSubmitConfirmDialog() {
        isInternalDialogShowing = true
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Submit Test?")
        builder.setMessage("Are you sure you want to submit the test?")
        builder.setPositiveButton("Yes") { _, _ ->
            isInternalDialogShowing = false
            submitQuiz()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            isInternalDialogShowing = false
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun submitQuiz() {
        isSubmitting = true
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        saveCurrentAnswer() // Ensure last action is captured

        var score = 0
        val total = quiz.quizList?.size ?: 0

        for (i in 0 until total) {
            val correctAns = quiz.quizList?.get(i)?.CorrectAnswer ?: ""
            val userAns = userAnswers[i]
            
            // Checking like "Option 3" == "Option 3"
            if (userAns?.trim().equals(correctAns.trim(), ignoreCase = true)) {
                score++
            }
        }

        Toast.makeText(this, "Quiz has been submitted.", Toast.LENGTH_SHORT).show()
        
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra("FINAL_SCORE", score)
        intent.putExtra("TOTAL_QUESTIONS", total)
        startActivity(intent)
        finish()
    }

    private fun startTimer() {
        val durationMins = (quiz.quizDuration?.toLongOrNull() ?: 15L).coerceAtLeast(1L)
        timer = object : CountDownTimer(durationMins * 60000, 1000) { // Convert minutes to milliseconds
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar3.progress = millisUntilFinished.toInt()
                val totalSecondsLeft = millisUntilFinished / 1000
                val minutesLeft = totalSecondsLeft / 60
                val secondsLeft = totalSecondsLeft % 60
                binding.editTextTime.text = "${minutesLeft}:${String.format("%02d", secondsLeft)}" // Format seconds to always display 2 digits
            }

            override fun onFinish() {
                Toast.makeText(this@OngoingQuizActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                submitQuiz() // Auto-submit when time finishes
            }
        }
        timer.start()
    }

    private fun showAlertDialog() {
        isInternalDialogShowing = true
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Submit Quiz?")
        builder.setMessage("Are you sure you want to leave? This action will forcefully submit your active quiz.")
        builder.setPositiveButton("Submit") { _, _ ->
            isInternalDialogShowing = false
            submitQuiz()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            isInternalDialogShowing = false
            dialog.dismiss() 
            startTimer() 
        }
        builder.setCancelable(false) 
        builder.create().show()
    }

    override fun onBackPressed() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        showAlertDialog()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (isQuizStarted && !isSubmitting) {
            Toast.makeText(this, "Quiz forcefully submitted due to app switching/minimizing!", Toast.LENGTH_LONG).show()
            submitQuiz()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus && isQuizStarted && !isSubmitting && !isInternalDialogShowing) {
            Toast.makeText(this, "Proctoring Warning: Please do not change windows or pull down notifications!", Toast.LENGTH_SHORT).show()
            submitQuiz()
        }
    }
}
