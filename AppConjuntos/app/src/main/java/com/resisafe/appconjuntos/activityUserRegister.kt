package com.resisafe.appconjuntos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class activityUserRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        val registerButton: Button = findViewById(R.id.registerButton)
        var cedulaCampo: TextView = findViewById(R.id.cedulaField);
        var nombreCampo: TextView = findViewById(R.id.nombreField);
        var apellidoCampo: TextView = findViewById(R.id.apellidoField);
        var contrasenaCamo: TextView = findViewById(R.id.contrasenaField);
        registerButton.setOnClickListener() {
            val userData = UsuarioRegisterModel(
                nombreCampo.text.toString(),
                apellidoCampo.text.toString(),
                cedulaCampo.text.toString().toInt(),
                contrasenaCamo.text.toString()
            )
            val apiService = RetrofitClient.apiService

            apiService.registerUser(userData).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.d("Tag", responseBody.toString())
                        } else {
                            Log.e("Tag", "Response body is null")
                        }
                    } else {
                        Log.e("Tag", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("Tag", "Failed to make request: ${t.message}", t)
                }
            })
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }
    }
}