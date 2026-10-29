package com.example.reater.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reater.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        if (intent.getBooleanExtra("SESSION_EXPIRED", false)) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show()
        }
    }
}