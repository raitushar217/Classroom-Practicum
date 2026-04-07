package com.example.reater.ui.compose.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.reater.models.Coursework
import com.example.reater.models.Question
import com.example.reater.models.Quiz
import com.example.reater.models.Student
import com.example.reater.ui.compose.components.ClassCard
import com.example.reater.ui.compose.components.QuizCard
import com.example.reater.ui.compose.screens.HomeScreen
import com.example.reater.ui.compose.screens.ProfileScreen
import com.example.reater.ui.compose.screens.QuizScreen
import com.example.reater.ui.compose.theme.ReaterTheme

// Sample data for previews
private val sampleCourses = listOf(
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
        "IT2130",
        "Fundamentals of DataScience",
        "IT-E",
        "TID0052",
        "Dr. Veena Khandelwal",
        "1",
        "3rd Semester",
        "JUL-NOV 2022"
    )
)

private val sampleStudent = Student(
    enrollmentID = "2021IT001",
    fullName = "John Doe",
    degreeID = "B.Tech IT",
    classID = "IT-E",
    phoneNumber = "+91 9876543210",
    enrollmentYear = "2021",
    email = "john.doe@example.com",
    dob = "15/08/2003"
)

private val sampleQuestions = listOf(
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
    )
)

private val sampleQuizzes = listOf(
    Quiz(
        "Monday",
        "Mathematics",
        "3",
        "MATH101",
        sampleQuestions,
        "Differential Equations Quiz",
        "IT-E"
    ),
    Quiz(
        "Wednesday",
        "Computer Networks",
        "2",
        "IT2101",
        sampleQuestions,
        "Network Protocols Quiz",
        "IT-E"
    )
)

// Component Previews
@Preview(showBackground = true, name = "Class Card")
@Composable
fun ClassCardPreview() {
    ReaterTheme {
        ClassCard(
            coursework = sampleCourses[0],
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Quiz Card")
@Composable
fun QuizCardPreview() {
    ReaterTheme {
        QuizCard(
            quiz = sampleQuizzes[0],
            onClick = {}
        )
    }
}

// Screen Previews
@Preview(showBackground = true, name = "Home Screen")
@Composable
fun HomeScreenPreview() {
    ReaterTheme {
        HomeScreen(
            courses = sampleCourses,
            studentName = "John Doe",
            onClassClick = {},
            onProfileClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Home Screen - Empty")
@Composable
fun HomeScreenEmptyPreview() {
    ReaterTheme {
        HomeScreen(
            courses = emptyList(),
            studentName = "John Doe",
            onClassClick = {},
            onProfileClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Profile Screen")
@Composable
fun ProfileScreenPreview() {
    ReaterTheme {
        ProfileScreen(
            student = sampleStudent,
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Quiz Screen")
@Composable
fun QuizScreenPreview() {
    ReaterTheme {
        QuizScreen(
            quizzes = sampleQuizzes,
            onQuizClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Quiz Screen - Empty")
@Composable
fun QuizScreenEmptyPreview() {
    ReaterTheme {
        QuizScreen(
            quizzes = emptyList(),
            onQuizClick = {},
            onBackClick = {}
        )
    }
}
