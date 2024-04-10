package com.resisafe.appconjuntos

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


object RetrofitClient {
        private const val BASE_URL = "https://more-molly-honest.ngrok-free.app/api/"

        val apiService: ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiService::class.java)
        }
    }

interface ApiService {
    @POST("Users/Register")
    fun registerUser(@Body usuarioRegister: UsuarioRegisterModel): Call<ApiResponse>
    @POST("Users/Login")
    fun loginUser(@Body usuarioLogin: UsuarioLoginModel): Call<LoginResponse>
    @GET("Users/{id}")
    fun getUserData(@Path("id") userId: Int,@Header("Authorization") token: String): Call<UserData>
    @POST("Perfiles/CrearPerfil")
    fun createProfile(@Header("Authorization") token: String) : Call<ProfileData>
}

data class ApiResponse(val message: String)
data class LoginResponse(val token: String, val userID :Int)
data class ErrorResponse(val title: String, val status: Int, val detail: String)
data class UserData(  val idUsuario: Int,
                      val nombre: String,
                      val apellido: String,
                      val cedula: Int,
                      val contraseña: String)
data class ProfileData(val idPerfil: Int,val idUsuario: Int,val idConjunto: Int,val idTipoperfil: Int )
