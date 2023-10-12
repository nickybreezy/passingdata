package com.example.passingdata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameInputLayout = findViewById<TextInputLayout>(R.id.nameInputLayout)
        val nameEditText = findViewById<TextInputEditText>(R.id.nameEditText)
        val nextButton = findViewById<Button>(R.id.nextButton)

        nextButton.setOnClickListener {
            val name = nameEditText.text.toString()
            if (name.isNotEmpty()) {
                val intent = Intent(this, DisplayNameActivity::class.java)
                intent.putExtra("user_name", name)
                startActivity(intent)
            } else {
                nameInputLayout.error = "Name is required"
            }
        }
    }
}
