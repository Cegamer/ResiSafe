package com.resisafe.appresisafe

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListaReservasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaReservasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_reservas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.actionBar?.title = "Lista de Reservas"


        val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token!!
        val apiService = RetrofitClient.apiService
        val args = arguments
        val tipoSolicitanteArg = args?.getInt("tipoSolicitante")!!
        val idPrefilActual = args?.getInt("idPrefilActual")!!
        val idZonaComun = args?.getInt("idZonaComun")!!


        val layout = view.findViewById<LinearLayout>(R.id.layoutListaReservas)


        fun renderizarDatos(datos: List<ReservaLista>) {
            for (reserva in datos) {
                val cardView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_reservas, null) as CardView
                val nombreReservante = cardView.findViewById<TextView>(R.id.nombreReservante)
                val nombreZonaComun = cardView.findViewById<TextView>(R.id.nombreZonaComun)
                val fechaReserva = cardView.findViewById<TextView>(R.id.fechaReserva)
                val horarioInicio = cardView.findViewById<TextView>(R.id.horarioInicio)
                val horarioFin = cardView.findViewById<TextView>(R.id.horarioFin)
                val button = cardView.findViewById<Button>(R.id.button)

                nombreReservante.text = reserva.nombreReservante + " " + reserva.apellidoReservante
                nombreZonaComun.text = reserva.nombreZonaComun
                fechaReserva.text = reserva.fechaReserva
                horarioInicio.text = reserva.horaInicio
                horarioFin.text = reserva.horaFin


                if (tipoSolicitanteArg == 2) {
                    if (reserva.estado == 5) {
                        button.setBackgroundColor(0xFF7C7C7C.toInt()); button.text = "Cancelada"
                    } else {
                        button.text = "Cancelar"
                        button.setBackgroundColor(0xFFFF5722.toInt()) // Naranja
                        button.setOnClickListener() {
                            val builder = AlertDialog.Builder(view.context)
                            builder.setMessage("¿Seguro que desea cancelar la reserva?")
                            builder.setPositiveButton("Si") { dialog, _ ->
                                cambiarEstadoReserva(reserva.idReserva, 5, token, apiService)
                                layout.removeAllViews()
                                viewLifecycleOwner.lifecycleScope.launch() {
                                    val listaReservas =
                                        cargarReservasUsuario(idPrefilActual, token, apiService)!!
                                    renderizarDatos(listaReservas)
                                }
                                dialog.dismiss()
                            }
                            builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                            val dialog = builder.create()
                            dialog.show()
                        }
                    }

                } else {
                    button.setOnClickListener() {
                        val builderAlert = AlertDialog.Builder(context)
                        val opciones: Array<String> =
                            arrayOf("Pendiente", "Activa", "Finalizada", "Expirada", "Cancelada")
                        var defaultPosition = 0
                        builderAlert.setTitle("Seleccionar estado de Reserva")
                        builderAlert.setPositiveButton("Seleccionar") { dialog, _ ->
                            cambiarEstadoReserva(
                                reserva.idReserva,
                                defaultPosition + 1,
                                token,
                                apiService
                            )
                            layout.removeAllViews()
                            viewLifecycleOwner.lifecycleScope.launch() {
                                val listaReservas =
                                    cargarReservasZonaComun(idZonaComun, token, apiService)!!
                                renderizarDatos(listaReservas)
                            }
                            dialog.dismiss()
                        }
                        builderAlert.setSingleChoiceItems(
                            opciones,
                            defaultPosition
                        ) { dialog, which ->
                            defaultPosition = which
                        }
                        builderAlert.show()
                    }
                    when (reserva.estado) {
                        1 -> {
                            button.text = "Pendiente"
                            button.setBackgroundColor(0xFFC107.toInt()) // Amarillo
                        }

                        2 -> {
                            button.text = "Activa"
                            button.setBackgroundColor(0xFF4CAF50.toInt()) // Verde
                        }

                        3 -> {
                            button.text = "Finalizada"
                            button.setBackgroundColor(0xFF2196F3.toInt()) // Azul
                        }

                        4 -> {
                            button.text = "Expirada"
                            button.setBackgroundColor(0xFFFF5722.toInt()) // Naranja
                        }

                        5 -> {
                            button.text = "Cancelada"
                            button.setBackgroundColor(0xFF7C7C7C.toInt())  // gris
                        }
                    }
                }

                layout.addView(cardView)
            }
        }
        if (tipoSolicitanteArg == 2) {
            viewLifecycleOwner.lifecycleScope.launch() {
                val listaReservas = cargarReservasUsuario(idPrefilActual, token, apiService)!!
                renderizarDatos(listaReservas)
            }

        } else {
            viewLifecycleOwner.lifecycleScope.launch() {
                val listaReservas = cargarReservasZonaComun(idZonaComun, token, apiService)!!
                renderizarDatos(listaReservas)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private suspend fun cargarReservasZonaComun(
        idZonaComun: Int,
        token: String,
        apiService: ApiService
    ): List<ReservaLista>? {
        val listaVisitas =
            apiService.getReservasByZonaComun(idZonaComun, token).awaitResponse().body()
        return listaVisitas
    }

    private suspend fun cargarReservasUsuario(
        idUsuario: Int,
        token: String,
        apiService: ApiService
    ): List<ReservaLista>? {
        val listaVisitas =
            apiService.getReservasByUsuario(idUsuario, token).awaitResponse().body()
        return listaVisitas
    }

    fun cambiarEstadoReserva(
        idReserva: Int,
        estadoNuevo: Int,
        token: String,
        apiService: ApiService
    ) {
        apiService.cambiarEstadoReserva(idReserva, estadoNuevo, token)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val datos = response.body()
                        if (datos != null) {
                            val builder = AlertDialog.Builder(context)
                            builder.setMessage("Estado de la reserva modificado con éxito")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }
                            val dialog = builder.create()
                            dialog.show()
                        }
                    } else {
                        Log.e("Tag", "Response body is null")
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("Tag", "Response body is dsafadfafdasf")
                }
            })


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListaReservasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListaReservasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}