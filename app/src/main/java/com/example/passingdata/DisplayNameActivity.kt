package com.example.passingdata

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisplayNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_name)


        val displayNameTextView = findViewById<TextView>(R.id.displayNameTextView)

        val userName = intent.getStringExtra("user_name")
        displayNameTextView.text = userName
    }
}
