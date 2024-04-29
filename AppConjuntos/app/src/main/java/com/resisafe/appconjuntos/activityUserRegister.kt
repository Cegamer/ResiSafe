package com.resisafe.appconjuntos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class activityUserRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        val registerButton: Button = findViewById(R.id.registerButton)
        val cancelButton : Button = findViewById(R.id.cancelButton)
        var cedulaCampo: TextInputEditText = findViewById(R.id.cedulaField);
        var nombreCampo: TextInputEditText = findViewById(R.id.nombreField);
        var apellidoCampo: TextInputEditText = findViewById(R.id.apellidoField);
        var contrasenaCamo: TextInputEditText = findViewById(R.id.contrasenaField);
        cancelButton.setOnClickListener () {
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }

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