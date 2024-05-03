package com.resisafe.appconjuntos

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
import androidx.navigation.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val apiService = RetrofitClient.apiService
        val context = this.requireContext()

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

                nombreReservante.text = reserva.nombreReservante
                nombreZonaComun.text = reserva.nombreZonaComun
                fechaReserva.text = reserva.fecha
                horarioInicio.text = reserva.horaInicio
                horarioFin.text = reserva.horaFin

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
                }

                layout.addView(cardView)
            }
        }


        val listaReservas = listOf(
            ReservaLista(1, "Juan Pérez", "Zona Común 1", "2024-05-02", "09:00", "11:00", 5,1),
            ReservaLista(2, "María García", "Zona Común 1", "2024-05-03", "14:00", "16:00", 3,2),
            ReservaLista(3, "Luis Martínez", "Zona Común 1", "2024-05-04", "10:30", "12:30", 8,3),
            ReservaLista(4, "Ana Rodríguez", "Zona Común 1", "2024-05-05", "18:00", "20:00", 6,2),
            ReservaLista(5, "Pedro Sánchez", "Zona Común 1", "2024-05-06", "13:00", "15:00", 4,4)
        )

        renderizarDatos(listaReservas)

        //TODO: obtener datos desde el API
        /*
        apiService.obtenerConjuntos().enqueue(object :
            Callback<List<Conjunto>> {
            override fun onResponse(
                call: Call<List<Conjunto>>,
                response: Response<List<Conjunto>>
            ) {
                if (response.isSuccessful) {
                    val datos = response.body()!!


                } else {
                    Log.e("Tag", "Response body is null")
                }
            }

            override fun onFailure(call: Call<List<Conjunto>>, t: Throwable) {
                Log.e("Tag", "Response body is dsafadfafdasf")
            }
        })*/

        super.onViewCreated(view, savedInstanceState)
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