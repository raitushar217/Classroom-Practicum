package com.example.reater.data.network

import com.example.reater.models.LoginRequest
import com.example.reater.models.LoginResponse
import com.example.reater.models.SignupRequest
import com.example.reater.models.TeacherLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("api/users/login/")
    suspend fun StudentLoginRequest(@Body data: LoginRequest): Response<LoginResponse>

    @POST("api/users/register/student/")
    suspend fun StudentSignupRequest(@Body data: SignupRequest): Response<LoginResponse>

    @POST("api/users/login/")
    suspend fun TeacherLoginRequest(@Body data: LoginRequest): Response<TeacherLoginResponse>


}