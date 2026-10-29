package com.example.reater.teacherapp.fragments.TeacherQuiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.reater.R
import com.example.reater.viewmodels.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

data class QuizQuestion(
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String
)

@AndroidEntryPoint
class TeacherQuizFragment : Fragment() {

    private val args: TeacherQuizFragmentArgs by navArgs()
    private lateinit var mainViewModel: MainViewModel
    private val quizQuestions = mutableListOf<QuizQuestion>()
    private var currentQuestionNumber = 1

    private lateinit var etQuizName: TextInputEditText
    private lateinit var etQuizDuration: TextInputEditText
    private lateinit var tvQuestionNumber: TextView
    private lateinit var etQuestionText: TextInputEditText
    private lateinit var etOptionA: TextInputEditText
    private lateinit var etOptionB: TextInputEditText
    private lateinit var etOptionC: TextInputEditText
    private lateinit var etOptionD: TextInputEditText
    private lateinit var rgCorrectAnswer: RadioGroup
    private lateinit var btnAddNextQuestion: Button
    private lateinit var btnPublishQuiz: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teacher_quiz, container, false)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        etQuizName = view.findViewById(R.id.etQuizName)
        etQuizDuration = view.findViewById(R.id.etQuizDuration)
        tvQuestionNumber = view.findViewById(R.id.tvQuestionNumber)
        etQuestionText = view.findViewById(R.id.etQuestionText)
        etOptionA = view.findViewById(R.id.etOptionA)
        etOptionB = view.findViewById(R.id.etOptionB)
        etOptionC = view.findViewById(R.id.etOptionC)
        etOptionD = view.findViewById(R.id.etOptionD)
        rgCorrectAnswer = view.findViewById(R.id.rgCorrectAnswer)
        btnAddNextQuestion = view.findViewById(R.id.btnAddNextQuestion)
        btnPublishQuiz = view.findViewById(R.id.btnPublishQuiz)

        etQuizName.setText("${args.course.SubjectName} Quiz")
        etQuizDuration.setText("15")

        updateQuestionNumberText()

        btnAddNextQuestion.setOnClickListener {
            addCurrentQuestion()
        }

        btnPublishQuiz.setOnClickListener {
            publishQuiz()
        }

        return view
    }

    private fun addCurrentQuestion() {
        val questionText = etQuestionText.text.toString().trim()
        val optionA = etOptionA.text.toString().trim()
        val optionB = etOptionB.text.toString().trim()
        val optionC = etOptionC.text.toString().trim()
        val optionD = etOptionD.text.toString().trim()

        val selectedRadioId = rgCorrectAnswer.checkedRadioButtonId

        if (questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
            optionC.isEmpty() || optionD.isEmpty() || selectedRadioId == -1) {
            Toast.makeText(requireContext(), "Please fill all fields and select correct answer", Toast.LENGTH_SHORT).show()
            return
        }

        val radioButton = view?.findViewById<RadioButton>(selectedRadioId)
        val correctAnswer = radioButton?.text.toString()

        val question = QuizQuestion(
            questionText, optionA, optionB, optionC, optionD, correctAnswer
        )
        quizQuestions.add(question)
        currentQuestionNumber++
        updateQuestionNumberText()
        clearQuestionFields()

        Toast.makeText(requireContext(), "Question added. Total: ${quizQuestions.size}", Toast.LENGTH_SHORT).show()
    }

    private fun publishQuiz() {
        // Assume last question is added if fields are filled but "add" wasn't clicked
        val questionText = etQuestionText.text.toString().trim()
        val optionA = etOptionA.text.toString().trim()
        val optionB = etOptionB.text.toString().trim()
        val optionC = etOptionC.text.toString().trim()
        val optionD = etOptionD.text.toString().trim()
        val selectedRadioId = rgCorrectAnswer.checkedRadioButtonId

        if (questionText.isNotEmpty() && optionA.isNotEmpty() && optionB.isNotEmpty() &&
            optionC.isNotEmpty() && optionD.isNotEmpty() && selectedRadioId != -1) {
            addCurrentQuestion()
        }

        val quizName = etQuizName.text.toString().trim()
        val quizDuration = etQuizDuration.text.toString().trim()

        if (quizName.isEmpty() || quizDuration.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter Quiz Name and Duration", Toast.LENGTH_SHORT).show()
            return
        }

        if (quizQuestions.isEmpty()) {
            Toast.makeText(requireContext(), "Please add at least one question", Toast.LENGTH_SHORT).show()
            return
        }

        val questionsPayload = quizQuestions.map { q ->
            mapOf(
                "questionText" to q.questionText,
                "optionA" to q.optionA,
                "optionB" to q.optionB,
                "optionC" to q.optionC,
                "optionD" to q.optionD,
                "correctAnswer" to q.correctAnswer
            )
        }

        val body = mapOf(
            "subject_code" to args.course.SubjectID,
            "section_code" to args.course.ClassID,
            "quiz_name" to quizName,
            "time_limit_minutes" to (quizDuration.toIntOrNull() ?: 15),
            "questions" to questionsPayload
        )

        Toast.makeText(requireContext(), "Publishing quiz '$quizName' for ${args.course.SubjectName}...", Toast.LENGTH_SHORT).show()

        mainViewModel.publishSubjectQuiz(body) { success ->
            Toast.makeText(requireContext(), "Quiz published successfully!", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
    }

    private fun clearQuestionFields() {
        etQuestionText.text?.clear()
        etOptionA.text?.clear()
        etOptionB.text?.clear()
        etOptionC.text?.clear()
        etOptionD.text?.clear()
        rgCorrectAnswer.clearCheck()
    }

    private fun updateQuestionNumberText() {
        tvQuestionNumber.text = "Question $currentQuestionNumber"
    }
}