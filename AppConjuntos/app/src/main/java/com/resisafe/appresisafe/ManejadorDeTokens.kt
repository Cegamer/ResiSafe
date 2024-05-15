package com.resisafe.appresisafe

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.resisafe.appresisafe.ui.AppMaster.Adapters.PerfilUsuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.File

object ManejadorDeTokens {
    fun guardarTokenUsuario(context: Context, loginResponse: LoginResponse,perfilActual: Int = 0) {
        val jsonObject = mapOf(
            "token" to loginResponse.token,
            "userID" to loginResponse.userID,
            "perfilActualId" to perfilActual
        )
        Log.d("Token:", "${loginResponse.token} - ${loginResponse.userID}")

        val jsonString = Gson().toJson(jsonObject)
        val directorioAlmacenamientoInterno = context.filesDir
        val archivoDatos = File(directorioAlmacenamientoInterno, "datos.json")

        try {
            eliminarTokenUsuario(context)
            archivoDatos.writeText(jsonString)
            Log.d("Ruta", archivoDatos.absolutePath)
        } catch (e: Exception) {
            Log.e("Error", "Error al escribir en el almacenamiento interno: ${e.message}", e)
        }
    }

     suspend fun cargarPerfilActual(context: Context): PerfilesDTO? {
        val archivoDatos = File(context.filesDir, "datos.json")
        if (!archivoDatos.exists()) {
            return null // El archivo no existe
        }

        val jsonString = archivoDatos.readText()
        val jsonObject = Gson().fromJson(jsonString, Map::class.java)
        val idPerfil = (jsonObject["perfilActualId"] as? Double)?.toInt() ?: return null
        val apiService = RetrofitClient.apiService

        val token = cargarTokenUsuario(context)?.token ?: return null

        val perfil = try {
            val response = apiService.getPerfil(idPerfil, token).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
        return perfil
    }


    fun cargarTokenUsuario(context: Context): LoginResponse? {
        val archivoDatos = File(context.filesDir, "datos.json")
        if (!archivoDatos.exists()) {
            return null // El archivo no existe
        }

        val jsonString = archivoDatos.readText()

        val jsonObject = Gson().fromJson(jsonString, Map::class.java)
        val token = jsonObject["token"] as? String ?: return null
        val userID = (jsonObject["userID"] as? Double)?.toInt() ?: return null

        val bearer = "Bearer $token"
        return LoginResponse(bearer, userID)
    }

    fun eliminarTokenUsuario(context: Context) {
        val archivoDatos = File(context.filesDir, "datos.json")
        if (archivoDatos.exists()) {
            archivoDatos.delete()
        }
    }
}