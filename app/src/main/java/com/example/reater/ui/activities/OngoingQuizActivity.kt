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

    // Track user answers (Key: Question Index, Value: Selected Option String)
    private val userAnswers = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PROCTORING: Prevent screenshots and screen recording
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )

        binding = ActivityOngoingQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

         quiz = args.quiz
        binding.question=quiz.quizList[0]
        setupUi()

    }

    private fun setupUi() {
        binding.qnoRV.adapter=qadapter
        binding.qnoRV.layoutManager=LinearLayoutManager(this)
        binding.progressBar3.max=quiz.quizDuration.toInt()*60000
        
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
            if (currentQuestionIndex == quiz.quizList.size - 1) {
                submitQuiz()
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
            val selectedRadioButton = findViewById<RadioButton>(selectedId)
            userAnswers[currentQuestionIndex] = selectedRadioButton.text.toString()
        } else {
            // Remove entry if user cleared the choice (optional)
            userAnswers.remove(currentQuestionIndex)
        }
    }

    private fun updateQuestionUI() {
        binding.question = quiz.quizList[currentQuestionIndex]
        binding.radioGroup.clearCheck() // Clear selected radio button naturally

        // Restore previous answer if they revisit the question
        val previousAnswer = userAnswers[currentQuestionIndex]
        if (previousAnswer != null) {
            when (previousAnswer) {
                binding.radioButton1.text.toString() -> binding.radioButton1.isChecked = true
                binding.radioButton2.text.toString() -> binding.radioButton2.isChecked = true
                binding.radioButton3.text.toString() -> binding.radioButton3.isChecked = true
                binding.radioButton4.text.toString() -> binding.radioButton4.isChecked = true
            }
        }
        
        if (currentQuestionIndex == quiz.quizList.size - 1) {
            binding.nextQuestion.text = "Submit Quiz"
        } else {
            binding.nextQuestion.text = "Next Question"
        }
    }

    private fun submitQuiz() {
        timer.cancel()
        saveCurrentAnswer() // Ensure last action is captured

        var score = 0
        val total = quiz.quizList.size

        for (i in 0 until total) {
            val correctAns = quiz.quizList[i].CorrectAnswer
            val userAns = userAnswers[i]
            
            // Note: Trimming whitespace just in case options have trailing spaces
            if (userAns?.trim() == correctAns.trim()) {
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
        timer = object : CountDownTimer(quiz.quizDuration.toLong() * 60000, 1000) { // Convert minutes to milliseconds
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar3.progress= (millisUntilFinished).toInt()
                val totalSecondsLeft = millisUntilFinished / 1000
                val minutesLeft = totalSecondsLeft / 60
                val secondsLeft = totalSecondsLeft % 60
                binding.editTextTime.text = "${minutesLeft}:${String.format("%02d", secondsLeft)}" // Format seconds to always display 2 digits
            }

            override fun onFinish() {
                submitQuiz() // Auto-submit when time finishes
            }
        }
        timer.start()
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Submit Quiz?")
        builder.setMessage("Are you sure you want to leave? This action will forcefully submit your active quiz.")
        builder.setPositiveButton("Submit") { _, _ ->
            submitQuiz()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss() 
            startTimer() 
        }
        builder.setCancelable(false) 
        builder.create().show()
    }

    override fun onBackPressed() {
        timer.cancel() 
        showAlertDialog()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Toast.makeText(this, "Quiz forcefully submitted due to app switching/minimizing!", Toast.LENGTH_LONG).show()
        submitQuiz()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) {
            Toast.makeText(this, "Proctoring Warning: Please do not change windows or open notifications!", Toast.LENGTH_SHORT).show()
        }
    }
}
