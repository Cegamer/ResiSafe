package com.resisafe.appconjuntos

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.File

object ManejadorDeTokens {
    fun guardarTokenUsuario(context: Context, loginResponse: LoginResponse) {
        val jsonObject = mapOf(
            "token" to loginResponse.token,
            "userID" to loginResponse.userID
        )

        Log.d("Token:", "${loginResponse.token} - ${loginResponse.userID}")

        val jsonString = Gson().toJson(jsonObject)

        // Obtener el directorio de almacenamiento interno
        val directorioAlmacenamientoInterno = context.filesDir

        // Crear un archivo en el directorio de almacenamiento interno
        val archivoDatos = File(directorioAlmacenamientoInterno, "datos.json")

        try {
            eliminarTokenUsuario(context)
            archivoDatos.writeText(jsonString)
            Log.d("Ruta", archivoDatos.absolutePath)
        } catch (e: Exception) {
            Log.e("Error", "Error al escribir en el almacenamiento interno: ${e.message}", e)
        }
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