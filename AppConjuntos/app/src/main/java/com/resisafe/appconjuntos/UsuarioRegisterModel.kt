package com.resisafe.appconjuntos

data class UsuarioRegisterModel (
        val nombre: String,
        val apellido: String,
        val cedula: Int,
        val contraseña: String
)

data class UsuarioLoginModel(
        val cedula:Int,
        val contraseña:String
)