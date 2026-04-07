package com.example.reater.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.reater.models.Coursework
import com.example.reater.models.Question
import com.example.reater.models.Quiz
import com.example.reater.models.Student
import com.example.reater.ui.compose.screens.HomeScreen
import com.example.reater.ui.compose.screens.ProfileScreen
import com.example.reater.ui.compose.screens.QuizScreen
import com.example.reater.ui.compose.theme.ReaterTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Sample Activity demonstrating pure Compose implementation
 * This can be used as an alternative to Fragment-based navigation
 */
@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ReaterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReaterApp()
                }
            }
        }
    }
}

@Composable
fun ReaterApp() {
    // Simple navigation state - in production, use Navigation Compose
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    
    when (currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                courses = getSampleCourses(),
                studentName = "John Doe",
                onClassClick = { coursework ->
                    // Navigate to class details
                },
                onProfileClick = {
                    currentScreen = Screen.Profile
                }
            )
        }
        is Screen.Profile -> {
            ProfileScreen(
                student = getSampleStudent(),
                onBackClick = {
                    currentScreen = Screen.Home
                }
            )
        }
        is Screen.Quiz -> {
            QuizScreen(
                quizzes = getSampleQuizzes(),
                onQuizClick = { quiz ->
                    // Navigate to quiz details
                },
                onBackClick = {
                    currentScreen = Screen.Home
                }
            )
        }
    }
}

sealed class Screen {
    object Home : Screen()
    object Profile : Screen()
    object Quiz : Screen()
}

// Sample data functions
private fun getSampleCourses() = listOf(
    Coursework(
        "IT2101",
        "COMPUTER-NETWORKS",
        "IT-E",
        "TID001",
        "Dr. Sudhir Sharma",
        "4",
        "3rd Semester",
        "JUL-NOV 2021"
    ),
    Coursework(
        "IT2104",
        "DATA COMMUNICATIONS (PC-4)",
        "IT-E",
        "TID0054",
        "Dr. Pankaj Vyas",
        " ",
        "5th Semester",
        "JUL-NOV 2021"
    ),
    Coursework(
        "IT2130",
        "Fundamentals of DataScience",
        "IT-E",
        "TID0052",
        "Dr. Veena Khandelwal",
        "1",
        "3rd Semester",
        "JUL-NOV 2022"
    ),
    Coursework(
        "IT2131",
        "Operating Systems",
        "IT-E",
        "TID005",
        "Dr. Devesh Choudhary",
        " ",
        "4th Semester",
        "JUL-NOV 2022"
    ),
    Coursework(
        "MA2101",
        "ENGINEERING MATHEMATICS – III",
        "IT-E",
        "TID001",
        "Dr. Sudhir Sharma",
        " ",
        "5th Semester",
        "JUL-NOV 2022"
    )
)

private fun getSampleStudent() = Student(
    enrollmentID = "2021IT001",
    fullName = "John Doe",
    degreeID = "B.Tech IT",
    classID = "IT-E",
    phoneNumber = "+91 9876543210",
    enrollmentYear = "2021",
    email = "john.doe@example.com",
    dob = "15/08/2003"
)

private fun getSampleQuizzes(): List<Quiz> {
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
        )
    )
    
    return listOf(
        Quiz(
            "Monday",
            "Mathematics",
            "3",
            "MATH101",
            questionsList,
            "Differential Equations Quiz",
            "IT-E"
        ),
        Quiz(
            "Wednesday",
            "Computer Networks",
            "2",
            "IT2101",
            questionsList,
            "Network Protocols Quiz",
            "IT-E"
        ),
        Quiz(
            "Friday",
            "Data Science Fundamentals",
            "2.5",
            "IT2130",
            questionsList,
            "Python & Statistics Quiz",
            "IT-E"
        )
    )
}
