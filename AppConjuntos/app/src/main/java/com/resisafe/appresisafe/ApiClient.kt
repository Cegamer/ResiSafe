package com.resisafe.appresisafe

import com.resisafe.appresisafe.ui.AppMaster.Adapters.PerfilUsuario
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
    private const val BASE_URL = " https://more-molly-honest.ngrok-free.app/api/"


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
    fun eliminarConjunto(
        @Path("id") conjuntoId: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @PUT("Conjuntos/{id}")
    fun eliminarConjunto(
        @Path("id") conjuntoId: Int,
        @Body conjunto: Conjunto,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

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
    fun getZonasComunesConjunto(
        @Path("idConjunto") idConjunto: Int,
        @Header("Authorization") token: String
    ): Call<List<ZonaComun>>

    @GET("Zonacomun/{idZonaComun}")
    fun getZonaComunData(
        @Path("idZonaComun") idZonaComun: Int,
        @Header("Authorization") token: String
    ): Call<ZonaComun>

    @DELETE("Zonacomun/{idZonaComun}")
    fun deleteZonaComun(
        @Path("idZonaComun") idZonaComun: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @GET("TiposQuejasReclamos")
    fun getTiposQUejas(): Call<List<tiposQuejas>>

    @POST("QuejasReclamos")
    fun postQuejasReclamos(
        @Body body: quejaReclamo,
        @Header("Authorization") token: String
    ): Call<ApiResponse>


    @POST("Visitantes")
    fun registrarVisitante(
        @Body body: visitante,
        @Header("Authorization") token: String
    ): Call<ApiResponse>


    @GET("Perfiles/BuscarCedula/{cedula}/Conjunto/{idConjunto}")
    fun getPerfilByCedula(@Path("cedula") cedula: Int,
                          @Path("idConjunto") idConjunto: Int,
                          @Header("Authorization") token: String): Call<perfilByCedula>
    @GET("Visitantes/BuscarCedula/{cedula}")
    fun getvisitanteByCedula( @Path("cedula") cedula: Int,
                        @Header("Authorization") token: String): Call<visitante>
    @POST("RegistroVisitantes")
    fun registrarVisita(
        @Body body: Visita,
        @Header("Authorization") token: String
    ) : Call<ApiResponse>

    @GET("RegistroVisitantes/Conjunto/{idConjunto}")
    fun getVisitantesByConjunto( @Path("idConjunto") idConjunto: Int, @Header("Authorization") token: String): Call<List<VisitaData>>
    @GET("RegistroVisitantes/Residente/{idResidente}")
    fun getVisitantesByResidente( @Path("idResidente") idResidente: Int, @Header("Authorization") token: String): Call<List<VisitaData>>
    @POST("Reservas")
    fun registrarReserva(@Body reserva: Reserva, @Header("Authorization") token: String) : Call<ApiResponse>
    @PUT("Reservas/{idReserva}")
    fun cambiarEstadoReserva(@Path("idReserva") idReserva: Int,@Body estado: Int, @Header("Authorization") token: String) : Call<ApiResponse>
    @GET("Zonacomun/HorariosDisponibles/{idZonaComun}/{fecha}")
    fun getHorariosDisponiblesZonaComun(@Path("idZonaComun") idZonaComun: Int, @Path("fecha") fecha:String,@Header("Authorization") token: String) : Call<List<HorarioDisponible>>
    @GET("Reservas/Zonacomun/{idZonaComun}")
    fun getReservasByZonaComun(@Path("idZonaComun") idZonaComun: Int,@Header("Authorization") token: String) : Call<List<ReservaLista>>
    @GET("Reservas/Perfil/{idPerfil}")
    fun getReservasByUsuario(@Path("idPerfil") idPerfil: Int,@Header("Authorization") token: String) : Call<List<ReservaLista>>

    @POST("Paquetes")
    fun registrarPaquete(@Body paquete: Paquete, @Header("Authorization") token: String) : Call<ApiResponse>
    @GET("Paquetes/Residente/{idPerfil}")
    fun obtenerPaquetesByUsuario(@Path("idPerfil") idPerfil: Int,@Header("Authorization") token: String) : Call<List<Paquete>>

    @GET("Paquetes/Conjunto/{idConjunto}")
    fun obtenerPaquetesByConjunto(@Path("idConjunto") idConjunto: Int,@Header("Authorization") token: String) : Call<List<Paquete>>

    @GET("Paquetes/{idPaquete}")
    fun obtenerPaqueteById(@Path("idPaquete") idPaquete: Int,@Header("Authorization") token: String) : Call<Paquete>

    @PUT("Paquetes/{idPaquete}")
    fun editarEstadoPaquete(@Path("idPaquete") idPaquete: Int,@Body paquete: Paquete,@Header("Authorization") token: String) : Call<ApiResponse>

}