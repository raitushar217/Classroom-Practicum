package com.example.reater.data.network

import com.example.reater.models.Coursework
import com.example.reater.models.Notice
import com.example.reater.models.CreateNoticeRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface KojoTeacherApi {

    @GET("api/classes/teacher/")
    suspend fun getTeacherClasses(): Response<List<Coursework>>

    @Multipart
    @POST("api/assignments/")
    suspend fun createAssignment(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("due_date") dueDate: RequestBody,
        @Part("section_code") sectionCode: RequestBody,
        @Part("subject_code") subjectCode: RequestBody,
        @Part("total_marks") totalMarks: RequestBody,
        @Part assignmentPdf: MultipartBody.Part
    ): Response<Unit>

    @POST("api/notices/")
    suspend fun createNotice(@Body request: CreateNoticeRequest): Response<Notice>

    @GET("api/notices/")
    suspend fun getNotices(): Response<List<Notice>>

    @GET("api/grades/subject/{subjectCode}")
    suspend fun getSubjectGrades(@Path("subjectCode") subjectCode: String): Response<List<com.example.reater.models.GradeCard>>

    @PUT("api/grades/subject/{subjectCode}/student/{studentId}")
    suspend fun updateStudentGrade(
        @Path("subjectCode") subjectCode: String,
        @Path("studentId") studentId: String,
        @Body request: com.example.reater.models.UpdateGradeRequest
    ): Response<com.example.reater.models.GradeCard>

    @POST("api/quizzes/publish-subject-quiz")
    suspend fun publishSubjectQuiz(@Body body: Map<String, @JvmSuppressWildcards Any>): Response<Unit>

    // Assignment Submissions & Grading
    @GET("api/assignments/{assignmentId}/submissions")
    suspend fun getAssignmentSubmissions(@Path("assignmentId") assignmentId: String): Response<List<com.example.reater.models.AssignmentSubmission>>

    @PUT("api/assignments/submissions/{submissionId}/grade")
    suspend fun gradeAssignmentSubmission(
        @Path("submissionId") submissionId: Int,
        @Body body: com.example.reater.models.GradeSubmissionRequest
    ): Response<com.example.reater.models.AssignmentSubmission>

    // Attendance
    @GET("api/attendance/subject/{subjectCode}/students")
    suspend fun getAttendanceRoster(@Path("subjectCode") subjectCode: String): Response<List<com.example.reater.models.AttendanceStudentRoster>>

    @POST("api/attendance/subject/{subjectCode}/session")
    suspend fun recordAttendanceSession(
        @Path("subjectCode") subjectCode: String,
        @Body body: com.example.reater.models.RecordAttendanceRequest
    ): Response<com.example.reater.models.AttendanceSessionResponse>
}