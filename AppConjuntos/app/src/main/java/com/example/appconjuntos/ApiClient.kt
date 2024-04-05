package com.example.appconjuntos

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


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
    @POST("Users")
    fun createPost(@Body usuarioRegister: UsuarioRegisterModel): Call<ApiResponse>
}

data class ApiResponse(val message: String)
