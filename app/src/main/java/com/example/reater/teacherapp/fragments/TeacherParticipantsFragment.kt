package com.example.reater.teacherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.reater.models.*
import com.example.reater.ui.compose.theme.*
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TeacherParticipantsFragment : Fragment() {

    private val args: TeacherParticipantsFragmentArgs by navArgs()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.fetchTeacherSubjectGrades(args.course.SubjectID)
        mainViewModel.fetchTeacherAttendanceRoster(args.course.SubjectID)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReaterTheme {
                    var selectedTab by remember { mutableStateOf(args.initialTab) }
                    val gradesResponse by mainViewModel.teacherSubjectGradesResponse.observeAsState()
                    val rosterResponse by mainViewModel.teacherAttendanceRosterResponse.observeAsState()

                    Scaffold(
                        topBar = {
                            TeacherParticipantsTopBar(
                                subjectName = args.course.SubjectName,
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it },
                                onBackClick = { findNavController().popBackStack() }
                            )
                        },
                        containerColor = BackgroundLight
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            if (selectedTab == 0) {
                                // Grades & Assessment Tab
                                when (val res = gradesResponse) {
                                    is NetworkResult.Loading -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = PrimaryBlue)
                                        }
                                    }
                                    is NetworkResult.Error -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = res.message ?: "Failed to load student grades",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Red
                                            )
                                        }
                                    }
                                    is NetworkResult.Success -> {
                                        val cards = res.data ?: emptyList()
                                        if (cards.isEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                .padding(24.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "No enrolled students found for this subject.",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = TextSecondary
                                                )
                                            }
                                        } else {
                                            TeacherStudentGradesList(
                                                subjectCode = args.course.SubjectID,
                                                gradeCards = cards,
                                                onSaveGrade = { studentId, updatedAssessments, remarks ->
                                                    val req = UpdateGradeRequest(
                                                        assessments = updatedAssessments,
                                                        teacherRemarks = remarks
                                                    )
                                                    mainViewModel.updateStudentGrade(
                                                        subjectCode = args.course.SubjectID,
                                                        studentId = studentId,
                                                        request = req
                                                    ) {
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "Student marks saved and synced real-time!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    else -> {}
                                }
                            } else {
                                // Attendance & Roll Call Tab
                                when (val res = rosterResponse) {
                                    is NetworkResult.Loading -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = PrimaryBlue)
                                        }
                                    }
                                    is NetworkResult.Error -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = res.message ?: "Failed to load attendance roster",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Red
                                            )
                                        }
                                    }
                                    is NetworkResult.Success -> {
                                        val roster = res.data ?: emptyList()
                                        TeacherAttendanceRosterContent(
                                            subjectCode = args.course.SubjectID,
                                            roster = roster,
                                            onSubmitAttendance = { date, slot, records ->
                                                mainViewModel.recordAttendanceSession(
                                                    subjectCode = args.course.SubjectID,
                                                    date = date,
                                                    slot = slot,
                                                    records = records
                                                ) { success ->
                                                    if (success) {
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "Lecture attendance recorded and synced real-time!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "Failed to record attendance",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                        )
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeacherParticipantsTopBar(
    subjectName: String,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = subjectName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (selectedTab == 0) "Grades & Continuous Evaluation" else "Lecture Attendance & Shortage Tracking",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            // Primary Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = PrimaryBlue,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = PrimaryBlue,
                            height = 3.dp
                        )
                    }
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) },
                    text = {
                        Text(
                            text = "Grades & Marks",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == 0) PrimaryBlue else TextSecondary
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) },
                    text = {
                        Text(
                            text = "Attendance & Roll Call",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == 1) PrimaryBlue else TextSecondary
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TeacherStudentGradesList(
    subjectCode: String,
    gradeCards: List<GradeCard>,
    onSaveGrade: (studentId: String, assessments: List<AssessmentItem>, remarks: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(gradeCards, key = { it.studentId }) { card ->
            TeacherStudentGradeEditorCard(
                gradeCard = card,
                onSave = { updatedAssessments, remarks ->
                    onSaveGrade(card.studentId, updatedAssessments, remarks)
                }
            )
        }
    }
}

@Composable
private fun TeacherStudentGradeEditorCard(
    gradeCard: GradeCard,
    onSave: (List<AssessmentItem>, String) -> Unit
) {
    val marksState = remember(gradeCard) {
        mutableStateMapOf<String, String>().apply {
            gradeCard.assessments.forEach { a ->
                put(a.id, a.obtainedMarks.toInt().toString())
            }
        }
    }

    var remarksState by remember(gradeCard) {
        mutableStateOf(gradeCard.teacherRemarks)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = gradeCard.studentName.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Column {
                        Text(
                            text = gradeCard.studentName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Roll/ID: ${gradeCard.studentId}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                val isPending = gradeCard.grade.equals("Updating", ignoreCase = true) || !gradeCard.isPublished
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isPending) Color(0xFFFEF3C7) else PrimaryBlue.copy(alpha = 0.1f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isPending) "Updating" else gradeCard.grade,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isPending) Color(0xFFD97706) else PrimaryBlue
                        )
                        Text(
                            text = if (isPending) "Pending" else "${gradeCard.percentage}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isPending) Color(0xFFD97706) else PrimaryBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BackgroundLight)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Continuous Assessment Marks (Editable)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            gradeCard.assessments.forEach { assessment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = assessment.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Max Marks: ${assessment.maxMarks.toInt()} • Weight: ${assessment.weightage}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextTertiary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedTextField(
                        value = marksState[assessment.id] ?: "",
                        onValueChange = { marksState[assessment.id] = it },
                        modifier = Modifier.width(84.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Teacher Feedback / Remarks",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = remarksState,
                onValueChange = { remarksState = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
                placeholder = { Text("Add constructive feedback or praise for student...") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val updatedAssessments = gradeCard.assessments.map { a ->
                        val enteredMarks = marksState[a.id]?.toDoubleOrNull() ?: a.obtainedMarks
                        a.copy(obtainedMarks = enteredMarks)
                    }
                    onSave(updatedAssessments, remarksState)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Marks & Sync Real-Time",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun TeacherAttendanceRosterContent(
    subjectCode: String,
    roster: List<AttendanceStudentRoster>,
    onSubmitAttendance: (date: String, slot: String, records: List<AttendanceRecordItem>) -> Unit
) {
    val todayDate = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    var lectureDate by remember { mutableStateOf(todayDate) }
    var lectureSlot by remember { mutableStateOf("Lecture 1 (10:00 - 11:00 AM)") }

    // Attendance map studentId -> status ("present", "absent", "late")
    val attendanceMap = remember(roster) {
        mutableStateMapOf<String, String>().apply {
            roster.forEach { student ->
                put(student.studentId, "present")
            }
        }
    }

    val totalStudents = roster.size
    val shortageCount = roster.count { it.percentage < 75.0 && it.total > 0 }
    val avgAttendance = if (totalStudents > 0) {
        roster.map { it.percentage }.average()
    } else 0.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stats Overview Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Class Attendance Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AttendanceStatBox(
                            title = "Total Enrolled",
                            value = "$totalStudents",
                            color = PrimaryBlue,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AttendanceStatBox(
                            title = "Class Avg",
                            value = "${String.format(Locale.getDefault(), "%.1f", avgAttendance)}%",
                            color = if (avgAttendance >= 75) Color(0xFF10B981) else Color(0xFFEF4444),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AttendanceStatBox(
                            title = "Shortage Risk",
                            value = "$shortageCount",
                            color = if (shortageCount > 0) Color(0xFFEF4444) else Color(0xFF10B981),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Take Lecture Attendance Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Take Lecture Attendance",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        TextButton(
                            onClick = {
                                roster.forEach { student ->
                                    attendanceMap[student.studentId] = "present"
                                }
                            }
                        ) {
                            Text("Mark All Present", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = lectureDate,
                            onValueChange = { lectureDate = it },
                            label = { Text("Date (YYYY-MM-DD)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = PrimaryBlue)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = lectureSlot,
                            onValueChange = { lectureSlot = it },
                            label = { Text("Slot / Lecture #") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Outlined.Schedule, contentDescription = null, tint = PrimaryBlue)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        // Student Roster with attendance toggles
        item {
            Text(
                text = "Enrolled Students Roster (${roster.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        items(roster, key = { it.studentId }) { student ->
            val currentStatus = attendanceMap[student.studentId] ?: "present"
            val hasShortage = student.percentage < 75.0 && student.total > 0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (hasShortage) Color(0xFFEF4444).copy(alpha = 0.15f)
                                        else PrimaryBlue.copy(alpha = 0.12f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = student.studentName.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasShortage) Color(0xFFEF4444) else PrimaryBlue
                                )
                            }

                            Column {
                                Text(
                                    text = student.studentName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "ID: ${student.studentId} • ${student.attended}/${student.total} attended",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Percentage & Shortage badge
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (hasShortage) Color(0xFFFEE2E2) else Color(0xFFECFDF5)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (hasShortage) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                                Text(
                                    text = "${String.format(Locale.getDefault(), "%.1f", student.percentage)}%",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasShortage) Color(0xFFDC2626) else Color(0xFF059669)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Segmented Attendance Toggle: Present | Absent | Late
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AttendanceToggleButton(
                            label = "Present",
                            isSelected = currentStatus == "present",
                            activeColor = Color(0xFF10B981),
                            modifier = Modifier.weight(1f),
                            onClick = { attendanceMap[student.studentId] = "present" }
                        )

                        AttendanceToggleButton(
                            label = "Absent",
                            isSelected = currentStatus == "absent",
                            activeColor = Color(0xFFEF4444),
                            modifier = Modifier.weight(1f),
                            onClick = { attendanceMap[student.studentId] = "absent" }
                        )

                        AttendanceToggleButton(
                            label = "Late",
                            isSelected = currentStatus == "late",
                            activeColor = Color(0xFFF59E0B),
                            modifier = Modifier.weight(1f),
                            onClick = { attendanceMap[student.studentId] = "late" }
                        )
                    }
                }
            }
        }

        // Save Button at bottom
        item {
            Button(
                onClick = {
                    val records = roster.map { student ->
                        AttendanceRecordItem(
                            studentId = student.studentId,
                            studentName = student.studentName,
                            status = attendanceMap[student.studentId] ?: "present"
                        )
                    }
                    onSubmitAttendance(lectureDate, lectureSlot, records)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Save Attendance",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Submit Lecture Attendance & Sync",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AttendanceStatBox(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun AttendanceToggleButton(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(36.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) activeColor else Color(0xFFF1F5F9),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextSecondary
            )
        }
    }
}