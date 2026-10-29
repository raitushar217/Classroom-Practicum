package com.example.reater.data

import com.example.reater.data.network.KojoStudentApi
import com.example.reater.data.network.KojoTeacherApi
import com.example.reater.data.network.UserApi
import com.example.reater.models.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val kojoStudentAPI: KojoStudentApi,
    private val kojoTeacherApi: KojoTeacherApi,
    private val userApi: UserApi
) {

    suspend fun getStudentCourses(): Response<List<Coursework>> {
        return kojoStudentAPI.getStudentCourses()
    }

    suspend fun getAvailableClasses(): Response<List<AvailableClass>> {
        return kojoStudentAPI.getAvailableClasses()
    }

    suspend fun registerForClass(request: RegisterClassRequest): Response<Unit> {
        return kojoStudentAPI.registerForClass(request)
    }

    suspend fun getStudyMaterials(subjectCode: String? = null): Response<List<StudyMaterial>> {
        return kojoStudentAPI.getStudyMaterials(subjectCode)
    }

    suspend fun getTeacherClasses(): Response<List<Coursework>> {
        return kojoTeacherApi.getTeacherClasses()
    }

    suspend fun createTeacherAssignment(
        name: String,
        description: String,
        dueDate: String,
        sectionCode: String,
        subjectCode: String,
        totalMarks: Int,
        pdfFile: File
    ): Response<Unit> {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val duePart = dueDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val secPart = sectionCode.toRequestBody("text/plain".toMediaTypeOrNull())
        val subPart = subjectCode.toRequestBody("text/plain".toMediaTypeOrNull())
        val marksPart = totalMarks.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(
            "assignment_pdf",
            pdfFile.name,
            pdfFile.asRequestBody("application/pdf".toMediaTypeOrNull())
        )
        return kojoTeacherApi.createAssignment(namePart, descPart, duePart, secPart, subPart, marksPart, filePart)
    }

    suspend fun createNotice(request: CreateNoticeRequest): Response<Notice> {
        return kojoTeacherApi.createNotice(request)
    }

    suspend fun getNotices(): Response<List<Notice>> {
        return kojoTeacherApi.getNotices()
    }

    suspend fun getStudentClasses(queries: Map<String, String>): Response<Subjects> {
        return kojoStudentAPI.getStudentClassData(queries)
    }

    suspend fun SigninStudent(loginRequest: LoginRequest): Response<LoginResponse> {
        return userApi.StudentLoginRequest(loginRequest)
    }

    suspend fun RegisterStudent(signupRequest: SignupRequest): Response<LoginResponse> {
        return userApi.StudentSignupRequest(signupRequest)
    }

    suspend fun LoginTeacher(loginRequest: LoginRequest): Response<TeacherLoginResponse> {
        return userApi.TeacherLoginRequest(loginRequest)
    }

    suspend fun submitStudentQuiz(quizResult: QuizResult): Response<Unit> {
        return kojoStudentAPI.submitStudentQuiz(quizResult.id, quizResult)
    }

    suspend fun getQuizzes(queries: Map<String, String>): Response<List<Quiz>> {
        return kojoStudentAPI.getQuizzes(queries)
    }

    suspend fun getAssignments(queries: Map<String, String> = emptyMap()): Response<List<Assignment>> {
        return kojoStudentAPI.getAssignments(queries)
    }

    suspend fun turninAssignment(assignmentId: String, pdfFile: File): Response<Unit> {
        val idPart = assignmentId.toRequestBody("text/plain".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(
            "submission_pdf",
            pdfFile.name,
            pdfFile.asRequestBody("application/pdf".toMediaTypeOrNull())
        )
        return kojoStudentAPI.turninAssignment(idPart, filePart)
    }

    suspend fun getStudentGrades(): Response<List<GradeCard>> {
        return kojoStudentAPI.getStudentGrades()
    }

    suspend fun getSubjectQuizzes(subjectCode: String): Response<List<Quiz>> {
        return kojoStudentAPI.getSubjectQuizzes(subjectCode)
    }

    suspend fun getSubjectGradesForTeacher(subjectCode: String): Response<List<GradeCard>> {
        return kojoTeacherApi.getSubjectGrades(subjectCode)
    }

    suspend fun updateStudentGrade(
        subjectCode: String,
        studentId: String,
        request: UpdateGradeRequest
    ): Response<GradeCard> {
        return kojoTeacherApi.updateStudentGrade(subjectCode, studentId, request)
    }

    suspend fun publishSubjectQuiz(body: Map<String, Any>): Response<Unit> {
        return kojoTeacherApi.publishSubjectQuiz(body)
    }

    suspend fun getAssignmentSubmissions(assignmentId: String): Response<List<com.example.reater.models.AssignmentSubmission>> {
        return kojoTeacherApi.getAssignmentSubmissions(assignmentId)
    }

    suspend fun gradeAssignmentSubmission(submissionId: Int, request: com.example.reater.models.GradeSubmissionRequest): Response<com.example.reater.models.AssignmentSubmission> {
        return kojoTeacherApi.gradeAssignmentSubmission(submissionId, request)
    }

    suspend fun getAttendanceRoster(subjectCode: String): Response<List<com.example.reater.models.AttendanceStudentRoster>> {
        return kojoTeacherApi.getAttendanceRoster(subjectCode)
    }

    suspend fun recordAttendanceSession(subjectCode: String, request: com.example.reater.models.RecordAttendanceRequest): Response<com.example.reater.models.AttendanceSessionResponse> {
        return kojoTeacherApi.recordAttendanceSession(subjectCode, request)
    }

    suspend fun getStudentAttendance(): Response<List<com.example.reater.models.StudentAttendanceSummary>> {
        return kojoStudentAPI.getStudentAttendance()
    }
}
