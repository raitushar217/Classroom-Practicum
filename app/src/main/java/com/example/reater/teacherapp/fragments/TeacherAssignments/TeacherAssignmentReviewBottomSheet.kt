package com.example.reater.teacherapp.fragments.TeacherAssignments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import com.example.reater.models.Assignment
import com.example.reater.models.AssignmentSubmission
import com.example.reater.ui.compose.theme.*
import com.example.reater.utils.Constants
import com.example.reater.utils.FilePreviewHelper
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TeacherAssignmentReviewBottomSheet : BottomSheetDialogFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var assignment: Assignment? = null

    companion object {
        private const val ARG_ASSIGNMENT = "arg_assignment"

        fun newInstance(assignment: Assignment): TeacherAssignmentReviewBottomSheet {
            val fragment = TeacherAssignmentReviewBottomSheet()
            val args = Bundle().apply {
                putParcelable(ARG_ASSIGNMENT, assignment)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assignment = arguments?.getParcelable(ARG_ASSIGNMENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val currAssignment = assignment ?: return View(requireContext())
        val assignmentId = currAssignment.assignmentid ?: "1"

        mainViewModel.fetchAssignmentSubmissions(assignmentId)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReaterTheme {
                    val submissionsResponse by mainViewModel.assignmentSubmissionsResponse.observeAsState()

                    TeacherAssignmentReviewContent(
                        assignment = currAssignment,
                        submissionsResponse = submissionsResponse,
                        onClose = { dismiss() },
                        onPreviewTeacherPdf = {
                            FilePreviewHelper.openFile(requireContext(), currAssignment.assignmentPdf)
                        },
                        onPreviewStudentPdf = { submission ->
                            FilePreviewHelper.openFile(requireContext(), submission.submissionPdf)
                        },
                        onGradeSubmission = { subId, marks, feedback ->
                            mainViewModel.gradeAssignmentSubmission(subId, marks, feedback, assignmentId) { success ->
                                if (success) {
                                    Toast.makeText(requireContext(), "Grade & Feedback saved successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(requireContext(), "Failed to save grade", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TeacherAssignmentReviewContent(
    assignment: Assignment,
    submissionsResponse: NetworkResult<List<AssignmentSubmission>>?,
    onClose: () -> Unit,
    onPreviewTeacherPdf: () -> Unit,
    onPreviewStudentPdf: (AssignmentSubmission) -> Unit,
    onGradeSubmission: (subId: Int, marks: Int, feedback: String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = BackgroundLight
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = assignment.assignmentName ?: "Assignment",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Subject: ${assignment.subjectid ?: "Class"} • Due: ${assignment.dueDate ?: "No date"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Teacher Preview Button
            Button(
                onClick = onPreviewTeacherPdf,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    text = "📄 Preview Posted Assignment PDF",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            if (!assignment.instructions.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Instructions: ${assignment.instructions}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Student Submissions & Evaluation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            when (submissionsResponse) {
                is NetworkResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    }
                }
                is NetworkResult.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = submissionsResponse.message ?: "Failed to load submissions", color = Color.Red)
                    }
                }
                is NetworkResult.Success -> {
                    val list = submissionsResponse.data ?: emptyList()
                    if (list.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "📥", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No submissions received yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "Student submissions will appear here once turned in.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextTertiary
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(list, key = { it.id }) { sub ->
                                SubmissionGradingCard(
                                    submission = sub,
                                    totalMarks = assignment.totalMarks ?: 50,
                                    onPreviewStudentPdf = { onPreviewStudentPdf(sub) },
                                    onSaveGrade = { marks, feedback ->
                                        onGradeSubmission(sub.id, marks, feedback)
                                    }
                                )
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun SubmissionGradingCard(
    submission: AssignmentSubmission,
    totalMarks: Int,
    onPreviewStudentPdf: () -> Unit,
    onSaveGrade: (marks: Int, feedback: String) -> Unit
) {
    var marksText by remember(submission) {
        mutableStateOf(submission.marks?.toString() ?: "")
    }
    var feedbackText by remember(submission) {
        mutableStateOf(submission.feedback)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = submission.studentName.ifBlank { "Student ${submission.studentId}" },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Roll ID: ${submission.studentId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                val isGraded = submission.status.equals("graded", ignoreCase = true) || submission.marks != null
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isGraded) Color(0xFFD1FAE5) else Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = if (isGraded) "Graded: ${submission.marks}/$totalMarks" else "Submitted",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isGraded) Color(0xFF065F46) else Color(0xFFD97706)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Student preview button
            OutlinedButton(
                onClick = onPreviewStudentPdf,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "👀 Preview Student's Submission PDF", fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = marksText,
                    onValueChange = { marksText = it },
                    label = { Text("Score") },
                    placeholder = { Text("0-$totalMarks") },
                    modifier = Modifier.width(100.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    label = { Text("Private Feedback") },
                    placeholder = { Text("Add comments...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val m = marksText.toIntOrNull() ?: 0
                    onSaveGrade(m, feedbackText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Save Marks & Feedback", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
