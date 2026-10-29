package com.example.reater.data.network

import android.content.Context
import android.content.Intent
import com.example.reater.ui.activities.AuthenticationActivity
import com.example.reater.utils.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = tokenManager.getToken()
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        // If the token is rejected with 401 Unauthorized, automatically clear token and redirect to Login
        if (response.code == 401 && !token.isNullOrBlank()) {
            tokenManager.clear()
            val intent = Intent(context, AuthenticationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("SESSION_EXPIRED", true)
            }
            context.startActivity(intent)
        }

        return response
    }
}
