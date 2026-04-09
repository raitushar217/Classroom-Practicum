package com.example.reater.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.reater.R

class QuizResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        
        val score = intent.getIntExtra("FINAL_SCORE", 0)
        val total = intent.getIntExtra("TOTAL_QUESTIONS", 0)

        findViewById<TextView>(R.id.score_text).text = score.toString()
        findViewById<TextView>(R.id.total_text).text = "/ $total"
        
        findViewById<Button>(R.id.btn_finish).setOnClickListener {
            finish()
        }
    }
}
