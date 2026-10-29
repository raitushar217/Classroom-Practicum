package com.example.reater.utils

import android.content.Context
import com.example.reater.utils.Constants.Companion.PREFS_TOKEN_FILE
import com.example.reater.utils.Constants.Companion.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    companion object {
        private const val USER_NAME = "USER_NAME"
        private const val USER_ID = "USER_ID"
    }

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserInfo(id: String, name: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, id)
        editor.putString(USER_NAME, name)
        editor.apply()
    }

    fun getUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}