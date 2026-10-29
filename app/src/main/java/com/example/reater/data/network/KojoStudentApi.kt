package com.example.reater.data.network

import com.example.reater.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface KojoStudentApi {

    @GET("api/classes/student/")
    suspend fun getStudentCourses(): Response<List<Coursework>>

    @GET("api/classes/available/")
    suspend fun getAvailableClasses(): Response<List<AvailableClass>>

    @POST("api/classes/register/")
    suspend fun registerForClass(@Body request: RegisterClassRequest): Response<Unit>

    @GET("api/classes/materials/")
    suspend fun getStudyMaterials(@Query("subject_code") subjectCode: String? = null): Response<List<StudyMaterial>>

    @GET("api/sections/")
    suspend fun getStudentClassData(@QueryMap queries: Map<String, String>): Response<Subjects>

    @POST("api/quizzes/quizzes/{id}/submit_attempt/")
    suspend fun submitStudentQuiz(@retrofit2.http.Path("id") id: String, @Body quizResult: QuizResult): Response<Unit>

    @GET("api/quizzes/quizzes/")
    suspend fun getQuizzes(@QueryMap queries: Map<String, String>): Response<List<Quiz>>

    @GET("api/assignments/")
    suspend fun getAssignments(@QueryMap queries: Map<String, String> = emptyMap()): Response<List<Assignment>>

    @Multipart
    @POST("api/assignments/submissions/")
    suspend fun turninAssignment(
        @Part("assignment_id") assignmentId: RequestBody,
        @Part submissionPdf: MultipartBody.Part
    ): Response<Unit>

    @GET("api/grades/student/")
    suspend fun getStudentGrades(): Response<List<GradeCard>>

    @GET("api/quizzes/subject/{subjectCode}")
    suspend fun getSubjectQuizzes(@Path("subjectCode") subjectCode: String): Response<List<Quiz>>

    @GET("api/attendance/student/")
    suspend fun getStudentAttendance(): Response<List<com.example.reater.models.StudentAttendanceSummary>>
}