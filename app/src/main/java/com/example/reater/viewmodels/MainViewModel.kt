package com.example.reater.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reater.data.Repository
import com.example.reater.models.*
import com.example.reater.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    // Registered Courses for Student
    val studentCoursesResponse: MutableLiveData<NetworkResult<List<Coursework>>> = MutableLiveData()

    fun fetchStudentCourses() = viewModelScope.launch {
        studentCoursesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getStudentCourses()
                studentCoursesResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                studentCoursesResponse.value = NetworkResult.Error(e.localizedMessage ?: "Error connecting to server")
            }
        } else {
            studentCoursesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Available Classes for Registration
    val availableClassesResponse: MutableLiveData<NetworkResult<List<AvailableClass>>> = MutableLiveData()

    fun fetchAvailableClasses() = viewModelScope.launch {
        availableClassesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getAvailableClasses()
                availableClassesResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                availableClassesResponse.value = NetworkResult.Error(e.localizedMessage ?: "Error fetching classes")
            }
        } else {
            availableClassesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Register for Class
    val registerClassResponse: MutableLiveData<NetworkResult<Unit>> = MutableLiveData()

    fun registerForClass(request: RegisterClassRequest) = viewModelScope.launch {
        registerClassResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.registerForClass(request)
                if (response.isSuccessful) {
                    registerClassResponse.value = NetworkResult.Success(Unit)
                    fetchStudentCourses() // Refresh registered classes automatically
                } else {
                    registerClassResponse.value = NetworkResult.Error(response.message())
                }
            } catch (e: Exception) {
                registerClassResponse.value = NetworkResult.Error(e.localizedMessage ?: "Registration failed")
            }
        } else {
            registerClassResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Study Materials / Notes for Student
    val studyMaterialsResponse: MutableLiveData<NetworkResult<List<StudyMaterial>>> = MutableLiveData()

    fun fetchStudyMaterials(subjectCode: String? = null) = viewModelScope.launch {
        studyMaterialsResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getStudyMaterials(subjectCode)
                studyMaterialsResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                studyMaterialsResponse.value = NetworkResult.Error(e.localizedMessage ?: "Error fetching notes")
            }
        } else {
            studyMaterialsResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Teacher Classes
    val teacherCoursesResponse: MutableLiveData<NetworkResult<List<Coursework>>> = MutableLiveData()

    fun fetchTeacherCourses() = viewModelScope.launch {
        teacherCoursesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getTeacherClasses()
                teacherCoursesResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                teacherCoursesResponse.value = NetworkResult.Error(e.localizedMessage ?: "Error fetching teacher classes")
            }
        } else {
            teacherCoursesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Create Teacher Assignment
    val createAssignmentResponse: MutableLiveData<NetworkResult<Unit>> = MutableLiveData()

    fun createTeacherAssignment(
        name: String,
        description: String,
        dueDate: String,
        sectionCode: String,
        subjectCode: String,
        totalMarks: Int,
        pdfFile: java.io.File
    ) = viewModelScope.launch {
        createAssignmentResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.createTeacherAssignment(
                    name, description, dueDate, sectionCode, subjectCode, totalMarks, pdfFile
                )
                if (response.isSuccessful) {
                    createAssignmentResponse.value = NetworkResult.Success(Unit)
                } else {
                    createAssignmentResponse.value = NetworkResult.Error("Failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                createAssignmentResponse.value = NetworkResult.Error(e.localizedMessage ?: "Error creating assignment")
            }
        } else {
            createAssignmentResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Notices
    val noticesResponse: MutableLiveData<NetworkResult<List<com.example.reater.models.Notice>>> = MutableLiveData()

    fun fetchNotices() = viewModelScope.launch {
        noticesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getNotices()
                noticesResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                noticesResponse.value = NetworkResult.Error(e.localizedMessage ?: "Error fetching notices")
            }
        } else {
            noticesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    val createNoticeResponse: MutableLiveData<NetworkResult<com.example.reater.models.Notice>> = MutableLiveData()

    fun createNotice(title: String, description: String) = viewModelScope.launch {
        createNoticeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val request = com.example.reater.models.CreateNoticeRequest(title = title, description = description)
                val response = repository.remote.createNotice(request)
                if (response.isSuccessful) {
                    createNoticeResponse.value = NetworkResult.Success(response.body()!!)
                } else {
                    createNoticeResponse.value = NetworkResult.Error("Failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                createNoticeResponse.value = NetworkResult.Error(e.localizedMessage ?: "Error posting notice")
            }
        } else {
            createNoticeResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Assignments
    val studentAssignments: MutableLiveData<NetworkResult<List<Assignment>>> = MutableLiveData()

    fun getStudentAssignments(queries: Map<String, String> = emptyMap()) = viewModelScope.launch {
        studentAssignments.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getAssignments(queries)
                studentAssignments.value = handleApiResponse(response)
            } catch (e: Exception) {
                studentAssignments.value = NetworkResult.Error("Error connecting to the API")
            }
        } else {
            studentAssignments.value = NetworkResult.Error("No internet connection")
        }
    }

    fun postStudentAssignment(assignmentId: String, pdfFile: File) = viewModelScope.launch {
        turninAssignment(assignmentId, pdfFile)
    }

    private suspend fun turninAssignment(assignmentId: String, pdfFile: File) {
        if (hasInternetConnection()) {
            val response = repository.remote.turninAssignment(assignmentId, pdfFile)
            if (response.isSuccessful) {
                Toast.makeText(getApplication(), "Successfully submitted", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(getApplication(), "Cannot submit without internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    // Quizzes
    val StudentQuizzes: MutableLiveData<NetworkResult<List<Quiz>>> = MutableLiveData()

    fun getStudentQuizzes(queries: Map<String, String>) = viewModelScope.launch {
        StudentQuizzes.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getQuizzes(queries)
                StudentQuizzes.value = handleApiResponse(response)
            } catch (e: Exception) {
                StudentQuizzes.value = NetworkResult.Error("Error Connecting to the API")
            }
        } else {
            StudentQuizzes.value = NetworkResult.Error("No internet connection")
        }
    }

    fun postStudentQuiz(quizResult: QuizResult) = viewModelScope.launch {
        if (hasInternetConnection()) {
            val response = repository.remote.submitStudentQuiz(quizResult)
            if (response.isSuccessful) {
                Toast.makeText(getApplication(), "Successfully submitted", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(getApplication(), "Cannot submit without internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    // Student Grades (Subject-wise, read-only for student)
    val studentGradesResponse: MutableLiveData<NetworkResult<List<GradeCard>>> = MutableLiveData()

    fun fetchStudentGrades() = viewModelScope.launch {
        studentGradesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getStudentGrades()
                studentGradesResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                studentGradesResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to load grade card")
            }
        } else {
            studentGradesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Subject Quizzes (Scoped to Subject)
    val subjectQuizzesResponse: MutableLiveData<NetworkResult<List<Quiz>>> = MutableLiveData()

    fun fetchSubjectQuizzes(subjectCode: String) = viewModelScope.launch {
        subjectQuizzesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getSubjectQuizzes(subjectCode)
                subjectQuizzesResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                subjectQuizzesResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to load quizzes")
            }
        } else {
            subjectQuizzesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Teacher Grades Management for a Subject
    val teacherSubjectGradesResponse: MutableLiveData<NetworkResult<List<GradeCard>>> = MutableLiveData()

    fun fetchTeacherSubjectGrades(subjectCode: String) = viewModelScope.launch {
        teacherSubjectGradesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getSubjectGradesForTeacher(subjectCode)
                teacherSubjectGradesResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                teacherSubjectGradesResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to load subject grades")
            }
        } else {
            teacherSubjectGradesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Teacher Update Student Grade (Real-Time)
    val updateGradeResponse: MutableLiveData<NetworkResult<GradeCard>> = MutableLiveData()

    fun updateStudentGrade(
        subjectCode: String,
        studentId: String,
        request: UpdateGradeRequest,
        onSuccess: (() -> Unit)? = null
    ) = viewModelScope.launch {
        updateGradeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.updateStudentGrade(subjectCode, studentId, request)
                val result = handleApiResponse(response)
                updateGradeResponse.value = result
                if (result is NetworkResult.Success) {
                    onSuccess?.invoke()
                    fetchTeacherSubjectGrades(subjectCode)
                }
            } catch (e: Exception) {
                updateGradeResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to update grade")
            }
        } else {
            updateGradeResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Teacher Publish Subject Quiz
    val publishQuizResponse: MutableLiveData<NetworkResult<Unit>> = MutableLiveData()

    fun publishSubjectQuiz(body: Map<String, Any>, onDone: (Boolean) -> Unit = {}) = viewModelScope.launch {
        publishQuizResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.publishSubjectQuiz(body)
                if (response.isSuccessful) {
                    publishQuizResponse.value = NetworkResult.Success(Unit)
                    onDone(true)
                } else {
                    publishQuizResponse.value = NetworkResult.Error(response.message())
                    onDone(false)
                }
            } catch (e: Exception) {
                publishQuizResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to publish quiz")
                onDone(false)
            }
        } else {
            publishQuizResponse.value = NetworkResult.Error("No Internet Connection")
            onDone(false)
        }
    }

    // Student Attendance Summary
    val studentAttendanceResponse: MutableLiveData<NetworkResult<List<StudentAttendanceSummary>>> = MutableLiveData()

    fun fetchStudentAttendance() = viewModelScope.launch {
        studentAttendanceResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getStudentAttendance()
                studentAttendanceResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                studentAttendanceResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to fetch attendance")
            }
        } else {
            studentAttendanceResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Teacher Attendance Roster
    val teacherAttendanceRosterResponse: MutableLiveData<NetworkResult<List<AttendanceStudentRoster>>> = MutableLiveData()

    fun fetchTeacherAttendanceRoster(subjectCode: String) = viewModelScope.launch {
        teacherAttendanceRosterResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getAttendanceRoster(subjectCode)
                teacherAttendanceRosterResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                teacherAttendanceRosterResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to fetch attendance roster")
            }
        } else {
            teacherAttendanceRosterResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Teacher Record Attendance Session
    val recordAttendanceResponse: MutableLiveData<NetworkResult<AttendanceSessionResponse>> = MutableLiveData()

    fun recordAttendanceSession(
        subjectCode: String,
        date: String,
        slot: String,
        records: List<AttendanceRecordItem>,
        onDone: (Boolean) -> Unit = {}
    ) = viewModelScope.launch {
        recordAttendanceResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.recordAttendanceSession(subjectCode, RecordAttendanceRequest(date, slot, records))
                if (response.isSuccessful && response.body() != null) {
                    recordAttendanceResponse.value = NetworkResult.Success(response.body()!!)
                    fetchTeacherAttendanceRoster(subjectCode)
                    onDone(true)
                } else {
                    recordAttendanceResponse.value = NetworkResult.Error(response.message())
                    onDone(false)
                }
            } catch (e: Exception) {
                recordAttendanceResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to record attendance")
                onDone(false)
            }
        } else {
            recordAttendanceResponse.value = NetworkResult.Error("No Internet Connection")
            onDone(false)
        }
    }

    // Teacher View Submissions for an Assignment
    val assignmentSubmissionsResponse: MutableLiveData<NetworkResult<List<AssignmentSubmission>>> = MutableLiveData()

    fun fetchAssignmentSubmissions(assignmentId: String) = viewModelScope.launch {
        assignmentSubmissionsResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getAssignmentSubmissions(assignmentId)
                assignmentSubmissionsResponse.value = handleApiResponse(response)
            } catch (e: Exception) {
                assignmentSubmissionsResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to fetch submissions")
            }
        } else {
            assignmentSubmissionsResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // Teacher Grade Submission
    val gradeSubmissionResponse: MutableLiveData<NetworkResult<AssignmentSubmission>> = MutableLiveData()

    fun gradeAssignmentSubmission(
        submissionId: Int,
        marks: Int,
        feedback: String,
        assignmentId: String,
        onDone: (Boolean) -> Unit = {}
    ) = viewModelScope.launch {
        gradeSubmissionResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.gradeAssignmentSubmission(submissionId, GradeSubmissionRequest(marks, feedback))
                if (response.isSuccessful && response.body() != null) {
                    gradeSubmissionResponse.value = NetworkResult.Success(response.body()!!)
                    fetchAssignmentSubmissions(assignmentId)
                    onDone(true)
                } else {
                    gradeSubmissionResponse.value = NetworkResult.Error(response.message())
                    onDone(false)
                }
            } catch (e: Exception) {
                gradeSubmissionResponse.value = NetworkResult.Error(e.localizedMessage ?: "Failed to grade submission")
                onDone(false)
            }
        } else {
            gradeSubmissionResponse.value = NetworkResult.Error("No Internet Connection")
            onDone(false)
        }
    }

    private inline fun <reified T> handleApiResponse(response: Response<T>): NetworkResult<T> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error("API Key Limited.")
            }
            response.isSuccessful -> {
                val data = response.body()
                if (data != null) {
                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("Response body is null")
                }
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}