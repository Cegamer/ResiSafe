package com.resisafe.appconjuntos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class UserLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        val textoRegistrarse: TextView = findViewById(R.id.registrarseText)
        textoRegistrarse.setOnClickListener(){

            val intent = Intent(this,activityUserRegister::class.java)
            startActivity(intent)
        }
    }
}