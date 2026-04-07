package com.example.reater.ui.compose.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reater.models.Question
import com.example.reater.models.Quiz
import com.example.reater.ui.compose.screens.PremiumQuizScreen
import com.example.reater.ui.compose.theme.ReaterTheme
import com.example.reater.viewmodels.MainViewModel

class ComposeQuizFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReaterTheme {
                    // Sample quiz data - replace with actual data from ViewModel
                    val questionsList = listOf(
                        Question(
                            qid = "Q1",
                            "General Knowledge",
                            "K23120",
                            Questiontext = "What is the square root of 64?",
                            qOption1 = "4",
                            qOption2 = "6",
                            qOption3 = "8",
                            qOption4 = "10",
                            CorrectAnswer = "Option 3"
                        ),
                        Question(
                            qid = "Q2",
                            "General Knowledge",
                            "K23120",
                            Questiontext = "What is the capital of France?",
                            qOption1 = "London",
                            qOption2 = "Berlin",
                            qOption3 = "Paris",
                            qOption4 = "Rome",
                            CorrectAnswer = "Option 3"
                        ),
                        Question(
                            qid = "Q3",
                            "General Knowledge",
                            "K23120",
                            Questiontext = "What is the chemical symbol for water?",
                            qOption1 = "H2O",
                            qOption2 = "CO2",
                            qOption3 = "NaCl",
                            qOption4 = "O2",
                            CorrectAnswer = "Option 1"
                        ),
                        Question(
                            qid = "Q4",
                            "General Knowledge",
                            "K23120",
                            Questiontext = "Who wrote 'Romeo and Juliet'?",
                            qOption1 = "William Shakespeare",
                            qOption2 = "Charles Dickens",
                            qOption3 = "Jane Austen",
                            qOption4 = "F. Scott Fitzgerald",
                            CorrectAnswer = "Option 1"
                        ),
                        Question(
                            qid = "Q5",
                            "General Knowledge",
                            "K23120",
                            Questiontext = "What is the largest planet in our solar system?",
                            qOption1 = "Earth",
                            qOption2 = "Mars",
                            qOption3 = "Jupiter",
                            qOption4 = "Saturn",
                            CorrectAnswer = "Option 3"
                        )
                    )

                    val quizzes = listOf(
                        Quiz(
                            "Monday, 10:00 AM",
                            "Differential Equations & Transforms",
                            "2.5",
                            "MATH101",
                            questionsList,
                            "Mathematics Mid-Term Quiz",
                            "IT-E"
                        ),
                        Quiz(
                            "Wednesday, 2:00 PM",
                            "Network Protocols & Architecture",
                            "2",
                            "IT2101",
                            questionsList,
                            "Computer Networks Quiz",
                            "IT-E"
                        ),
                        Quiz(
                            "Friday, 11:00 AM",
                            "Python, Statistics & ML Basics",
                            "3",
                            "IT2130",
                            questionsList,
                            "Data Science Fundamentals",
                            "IT-E"
                        )
                    )

                    PremiumQuizScreen(
                        quizzes = quizzes,
                        onQuizClick = { quiz ->
                            // Navigate to quiz details or start quiz
                            // You can use Navigation component here
                        },
                        onBackClick = {
                            findNavController().navigateUp()
                        }
                    )
                }
            }
        }
    }
}
