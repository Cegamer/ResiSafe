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
    @GET("Users/BuscarCedula/{cedula}")
    fun getUserByCedula(@Path("cedula") cedula:Int, @Header("Authorization") token: String): Call<UserData>
    @POST("Perfiles/CrearPerfil")
    fun createProfile(@Body ProfileData:ProfileData,@Header("Authorization") token: String) : Call<ApiResponse>
    @GET("Perfiles/DatosPerfil/{id}")
    fun obtenerDatosPerfiles(@Path("id") userId: Int,@Header("Authorization") token: String) : Call<List<CardItem>>
    @POST("Conjuntos/CrearConjunto")
    fun crearConjunto(@Body conjunto: Conjunto, @Header("Authorization") token: String) : Call<ApiResponse>
    @GET("Conjuntos")
    fun obtenerConjuntos() : Call<List<Conjunto>>
    @GET("Conjuntos/{id}")
    fun obtenerInfoConjunto(@Path("id") idConjunto:Int,@Header("Authorization") token:String) : Call<Conjunto>
    @POST("Perfiles/IniciarPerfil/{id}")
    fun loginProfile(@Path("id") perfilId:Int,@Header("Authorization") token: String): Call<LoginResponse>
    @GET("TipoPerfil")
    fun getTiposPerfil() :Call<List<TipoPerfil>>
}

data class ApiResponse(val message: String)
data class LoginResponse(val token: String, val userID :Int)
data class ErrorResponse(val title: String, val status: Int, val detail: String)
data class UserData(  val idUsuario: Int,
                      val nombre: String,
                      val apellido: String,
                      val cedula: Int,
                      val contraseña: String,
                      var foto: String)
data class ProfileData(val IdPerfil: Int, val IdUsuario: Int, val IdConjunto: Int, val IdTipoPerfil: Int, val Activo: Int)
data class CardItem(val idPerfil:Int,val nombreConjunto:String,val nombreTipoPerfil:String)
data class Conjunto(
    val idConjunto: Int,
    val nombre: String,
    val direccion: String,
    val activo: Int
)
data class TipoPerfil(val idTipo :Int, val nombreTipo: String)
