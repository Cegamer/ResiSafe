package com.resisafe.appconjuntos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        val textoRegistrarse: TextView = findViewById(R.id.registrarseText)
        textoRegistrarse.setOnClickListener() {

            val intent = Intent(this, activityUserRegister::class.java)
            startActivity(intent)
        }

        val botonIniciarSesion: Button = findViewById(R.id.botonIniciarSesion)
        val contexto = this

        botonIniciarSesion.setOnClickListener() {

            val cedulaCampo: TextInputEditText = findViewById(R.id.cedulaFieldLogin)
            val contrasenaCampo: TextInputEditText = findViewById(R.id.contrasenaFieldLogin)
            val errorText: TextView = findViewById(R.id.errorText)

            val loginData = UsuarioLoginModel(
                cedulaCampo.text.toString().toInt(),
                contrasenaCampo.text.toString()
            )
            val apiService = RetrofitClient.apiService


            apiService.loginUser(loginData).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val token = responseBody.token
                            val userId = responseBody.userID
                            Log.d("Tag", "Token: $token, UserID: $userId")
                            ManejadorDeTokens.guardarTokenUsuario(contexto, responseBody)
                            Log.d("Tag", "Token Guardado")
                            CambiarAHomeUsuario()

                        } else {
                            Log.e("Tag", "Response body is null")
                        }
                    } else {
                        try {
                            val errorBody = response.errorBody()?.string()
                            val errorJson = Gson().fromJson(errorBody, ErrorResponse::class.java)
                            errorText.text = errorJson.title;
                            Log.e(
                                "Tag",
                                "Unsuccessful response: ${response.code()}, Title: ${errorJson.title}, Detail: ${errorJson.detail}"
                            )
                        } catch (e: Exception) {
                            Log.e("Tag", "Failed to parse error response: ${e.message}", e)
                            errorText.text = "No se ha podido comunicar con el servidor";

                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Tag", "Failed to make request: ${t.message}", t)
                }
            })
        }
    }

    fun CambiarAHomeUsuario() {
        val intent = Intent(this, UsuarioActivity::class.java)
        startActivity(intent)
    }
}