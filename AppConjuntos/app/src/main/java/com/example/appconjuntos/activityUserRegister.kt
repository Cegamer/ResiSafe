package com.example.appconjuntos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class activityUserRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener(){


        }
    }


}