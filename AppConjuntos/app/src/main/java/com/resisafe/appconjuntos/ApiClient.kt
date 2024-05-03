package com.resisafe.appconjuntos

import com.resisafe.appconjuntos.ui.AppMaster.Adapters.PerfilUsuario
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


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

    @GET("Users")
    fun getUsers(@Header("Authorization") token: String): Call<List<UserData>>

    @POST("Users/Login")
    fun loginUser(@Body usuarioLogin: UsuarioLoginModel): Call<LoginResponse>

    @GET("Users/{id}")
    fun getUserData(@Path("id") userId: Int, @Header("Authorization") token: String): Call<UserData>

    @DELETE("Users/{id}")
    fun deleteUser(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @GET("Users/BuscarCedula/{cedula}")
    fun getUserByCedula(
        @Path("cedula") cedula: Int,
        @Header("Authorization") token: String
    ): Call<UserData>

    @POST("Perfiles/CrearPerfil")
    fun createProfile(
        @Body ProfileData: ProfileData,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @GET("Perfiles/DatosPerfil/{id}")
    fun obtenerDatosPerfiles(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Call<List<CardItem>>

    @POST("Conjuntos/CrearConjunto")
    fun crearConjunto(
        @Body conjunto: Conjunto,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @DELETE("Conjuntos/{id}")
    fun eliminarConjunto(@Path("id") conjuntoId:Int, @Header("Authorization") token: String) : Call<ApiResponse>

    @PUT("Conjuntos/{id}")
    fun eliminarConjunto(@Path("id") conjuntoId:Int,
                         @Body conjunto: Conjunto,
                         @Header("Authorization") token: String) : Call<ApiResponse>

    @GET("Conjuntos")
    fun obtenerConjuntos(): Call<List<Conjunto>>

    @GET("Conjuntos/{id}")
    fun obtenerInfoConjunto(
        @Path("id") idConjunto: Int,
        @Header("Authorization") token: String
    ): Call<Conjunto>

    @GET("Perfiles/Conjunto/{idConjunto}")
    fun obtenerPerfilesConjunto(
        @Path("idConjunto") idConjunto: Int?,
        @Header("Authorization") token: String
    ): Call<List<PerfilUsuario>>

    @POST("Perfiles/IniciarPerfil/{id}")
    fun loginProfile(
        @Path("id") perfilId: Int,
        @Header("Authorization") token: String
    ): Call<LoginResponse>


    @GET("Perfiles/{id}")
    fun getPerfil(
        @Path("id") perfilId: Int,
        @Header("Authorization") token: String
    ): Call<PerfilesDTO>

    @DELETE("Perfiles/{id}")
    fun eliminarPerfil(
        @Path("id") perfilId: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @PUT("Perfiles/{id}")
    fun cambiarEstadoPerfil(
        @Body body: CambiarEstadoRequestBody,
        @Path("id") perfilId: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    data class CambiarEstadoRequestBody(
        val estado: Int
        // Puedes agregar más campos si los necesitas
    )

    @GET("TipoPerfil")
    fun getTiposPerfil(): Call<List<TipoPerfil>>

    @POST("Zonacomun")
    fun crearZonaComun(
        @Body zonacomun: ZonaComun,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @GET("Zonacomun/Conjunto/{idConjunto}")
    fun getZonasComunesConjunto(@Path("idConjunto") idConjunto: Int,
                                @Header("Authorization") token: String) : Call<List<ZonaComun>>


}

data class PerfilesDTO(
    var idPerfil: Int,
    var idUsuario: Int,
    var idConjunto: Int,
    var idTipoPerfil: Int,
    var activo: Byte = 0
)
data class ApiResponse(val message: String)
data class LoginResponse(val token: String, val userID: Int)
data class ErrorResponse(val title: String, val status: Int, val detail: String)
data class ZonaComun(
    val idZonaComun: Int,
    val idConjunto: Int,
    val nombre: String,
    val horarioApertura: String,
    val horarioCierre: String,
    val aforoMaximo: Int,
    val precio: Int,
    val idIcono: Int,
    val intervaloTurnos: Int
)


data class UserData(
    val idUsuario: Int,
    val nombre: String,
    val apellido: String,
    val cedula: Int,
    val contraseña: String,
    var foto: String
)

data class ProfileData(
    val IdPerfil: Int,
    val IdUsuario: Int,
    val IdConjunto: Int,
    val IdTipoPerfil: Int,
    val Activo: Int
)

data class CardItem(val idPerfil: Int, val nombreConjunto: String, val nombreTipoPerfil: String)
data class Conjunto(
    val idConjunto: Int,
    val nombre: String,
    val direccion: String,
    val activo: Int
)

data class TipoPerfil(val idTipo: Int, val nombreTipo: String)
data class Reserva(
    val idReserva: Int,
    val idReservante: Int,
    val idZonaComun: Int,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val cantidadPersonas: Int
)

data class ReservaLista(
    val idReserva: Int,
    val nombreReservante: String,
    val nombreZonaComun: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val cantidadPersonas: Int,
    val estado: Int
)