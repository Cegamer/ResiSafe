package com.resisafe.appresisafe

data class UsuarioRegisterModel(
    val nombre: String,
    val apellido: String,
    val cedula: Int,
    val contraseña: String
)

data class UsuarioLoginModel(
    val cedula: Int,
    val contraseña: String
)


data class HorarioDisponible(
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val cuposDisponibles: Int
)

data class Paquete(
    val idPaquete: Int,
    val torre: String,
    val apto: String,
    val idVigilanteRecibe: Int,
    val estado: Int,
    val idResidenteRecibe: Int?,
    val fechaEntrega: String,
    val horaEntrega: String,
    val fechaRecibido: String?,
    val horaRecibido: String?,
    val idConjunto: Int
)

data class Reserva(
    val idReserva: Int,
    val idReservante: Int,
    val idZonaComun: Int,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val cantidadPersonas: Int,
    val estado: Int
)

data class VisitaData(
    val idRegistro: Int,
    val nombre: String,
    val apellido: String,
    val cedula: Int,
    val fecha: String,
    val horaIngreso: String,
    val nombreResidente: String,
    val apellidoResidente: String,
    val cedulaResidente: Int
)

data class perfilByCedula(
    var idPerfil: Int,
    var idConjunto: Int,
    var nombreApellido: String
)

data class Visita(
    var idRegistro: Int,
    var idVisitante: Int,
    var idResidenteVinculado: Int,
    var idVigilanteQueRegistra: Int,
    var fecha: String,
    var horaIngreso: String,
    var horaSalida: String
)

data class PerfilesDTO(
    var idPerfil: Int,
    var idUsuario: Int,
    var idConjunto: Int,
    var idTipoPerfil: Int,
    var activo: Byte = 0
)

data class tiposQuejas(var idtiposQuejasReclamos: Int, var nombreTipo: String)
data class quejaReclamo(
    var idquejasReclamos: Int,
    var idTipo: Int,
    var quejaReclamo: String,
    var idConjunto: Int,
    var idPersonaQueEnvia: Int
)

data class visitante(
    var idVisitante: Int,
    var nombre: String,
    var apellido: String,
    var cedula: Int,
    var foto: String
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


data class ReservaLista(
    val idReserva: Int,
    val nombreZonaComun: String,
    val nombreReservante: String,
    val apellidoReservante: String,
    val cedulaReservante: Int,
    val fechaReserva: String,
    val horaInicio: String,
    val horaFin: String,
    val cantidadPersonas: Int,
    val estado: Int
)
