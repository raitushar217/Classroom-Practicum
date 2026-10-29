package com.example.reater.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import android.content.Intent
import com.example.reater.models.Announcement
import com.example.reater.models.StudyMaterial
import com.example.reater.ui.compose.screens.PremiumCourseDetailsScreen
import com.example.reater.ui.compose.theme.ReaterTheme
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val args: DetailsActivityArgs by navArgs()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val coursework = args.coursework

        mainViewModel.fetchNotices()
        mainViewModel.getStudentAssignments()
        mainViewModel.fetchStudyMaterials(coursework.SubjectID)
        mainViewModel.fetchStudentGrades()
        mainViewModel.fetchStudentAttendance()
        mainViewModel.fetchSubjectQuizzes(coursework.SubjectID)

        setContent {
            ReaterTheme {
                val noticesResponse by mainViewModel.noticesResponse.observeAsState()
                val assignmentsResponse by mainViewModel.studentAssignments.observeAsState()
                val filesResponse by mainViewModel.studyMaterialsResponse.observeAsState()
                val gradesResponse by mainViewModel.studentGradesResponse.observeAsState()
                val attendanceResponse by mainViewModel.studentAttendanceResponse.observeAsState()
                val quizzesResponse by mainViewModel.subjectQuizzesResponse.observeAsState()

                val announcements = remember(noticesResponse) {
                    when (val res = noticesResponse) {
                        is NetworkResult.Success -> {
                            val notices = res.data ?: emptyList()
                            notices.map { notice ->
                                val authorName = when {
                                    notice.author.isNotBlank() -> notice.author
                                    notice.teacherName.isNotBlank() -> notice.teacherName
                                    notice.teacher == "111" -> "Dr. John Doe"
                                    notice.teacher == "222" -> "Dr. Jane Smith"
                                    notice.teacher == "333" -> "Dr. Alan Turing"
                                    coursework.TeacherName.isNotBlank() -> coursework.TeacherName
                                    else -> "Teacher ${notice.teacher}"
                                }
                                Announcement(
                                    author = authorName,
                                    timeStamp = if (notice.createdAt.length >= 10) notice.createdAt.take(10) else notice.createdAt,
                                    authorpicture = "",
                                    description = if (notice.title.isNotBlank()) "${notice.title}\n\n${notice.description}" else notice.description
                                )
                            }
                        }
                        else -> emptyList()
                    }
                }

                val assignments = remember(assignmentsResponse) {
                    when (val res = assignmentsResponse) {
                        is NetworkResult.Success -> {
                            val list = res.data ?: emptyList()
                            val filtered = list.filter {
                                (it.subjectid ?: "").contains(coursework.SubjectName, ignoreCase = true) ||
                                (it.subjectid ?: "").contains(coursework.SubjectID, ignoreCase = true)
                            }
                            if (filtered.isNotEmpty()) filtered else list
                        }
                        else -> emptyList()
                    }
                }

                val studyMaterials = remember(filesResponse) {
                    when (val res = filesResponse) {
                        is NetworkResult.Success -> res.data ?: emptyList()
                        else -> emptyList()
                    }
                }

                val subjectGrade = remember(gradesResponse) {
                    when (val res = gradesResponse) {
                        is NetworkResult.Success -> {
                            res.data?.find {
                                it.subjectCode.equals(coursework.SubjectID, ignoreCase = true) ||
                                it.subjectName.contains(coursework.SubjectName, ignoreCase = true)
                            }
                        }
                        else -> null
                    }
                }

                val subjectAttendance = remember(attendanceResponse) {
                    when (val res = attendanceResponse) {
                        is NetworkResult.Success -> {
                            res.data?.find {
                                it.subjectCode.equals(coursework.SubjectID, ignoreCase = true) ||
                                it.subjectName.contains(coursework.SubjectName, ignoreCase = true)
                            }
                        }
                        else -> null
                    }
                }

                val quizzes = remember(quizzesResponse) {
                    when (val res = quizzesResponse) {
                        is NetworkResult.Success -> res.data ?: emptyList()
                        else -> emptyList()
                    }
                }

                PremiumCourseDetailsScreen(
                    coursework = coursework,
                    announcements = announcements,
                    assignments = assignments,
                    studyMaterials = studyMaterials,
                    quizzes = quizzes,
                    gradeCard = subjectGrade,
                    attendance = subjectAttendance,
                    onBackClick = {
                        finish()
                    },
                    onAnnouncementClick = { announcement ->
                        Toast.makeText(
                            this@DetailsActivity,
                            announcement.author,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onAssignmentSubmit = { assignment, file ->
                        val assignmentId = assignment.assignmentid ?: "1"
                        mainViewModel.postStudentAssignment(assignmentId, file)
                        Toast.makeText(
                            this@DetailsActivity,
                            "Submitting ${assignment.assignmentName ?: "Assignment"}...",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onFileClick = { material ->
                        Toast.makeText(
                            this@DetailsActivity,
                            "Opening ${material.filename}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onQuizClick = { quiz ->
                        val intent = Intent(this@DetailsActivity, OngoingQuizActivity::class.java).apply {
                            putExtra("quiz", quiz)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchNotices()
        mainViewModel.getStudentAssignments()
        mainViewModel.fetchStudyMaterials(args.coursework.SubjectID)
        mainViewModel.fetchStudentGrades()
        mainViewModel.fetchStudentAttendance()
        mainViewModel.fetchSubjectQuizzes(args.coursework.SubjectID)
    }
}