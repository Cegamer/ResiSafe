package com.resisafe.appresisafe

import android.os.Bundle
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
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistorialPaquetesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistorialPaquetesFragment : Fragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.actionBar?.title = "Lista de Paquetes"


        val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token!!
        val apiService = RetrofitClient.apiService
        val args = arguments
        val tipoSolicitanteArg = args?.getInt("tipoSolicitante")!!
        val idPrefilActual = args?.getInt("idPrefilActual")!!
        val idConjunto = args?.getInt("idConjunto")!!


        val layout = view.findViewById<LinearLayout>(R.id.layoutPaquetes)




        fun renderizarDatos(datos: List<Paquete>) {
            for (paquete in datos) {
                val cardView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_paquetes, null) as CardView
                val torre = cardView.findViewById<TextView>(R.id.torre)
                val apto = cardView.findViewById<TextView>(R.id.apto)
                val fecha = cardView.findViewById<TextView>(R.id.fecha)
                val horaIngreso = cardView.findViewById<TextView>(R.id.horaIngreso)
                val button = cardView.findViewById<Button>(R.id.button)
                val editarButton = cardView.findViewById<ImageButton>(R.id.editarButton)


                torre.text = paquete.torre
                apto.text = paquete.apto
                fecha.text = paquete.fechaEntrega
                horaIngreso.text = paquete.horaEntrega

                when (paquete.estado) {
                    1 -> {
                        button.text = "Portería"
                        button.setBackgroundColor(0xFFC107.toInt()) // Amarillo
                    }

                    2 -> {
                        button.text = "Recibido"
                        button.setBackgroundColor(0xFF4CAF50.toInt()) // Verde
                    }
                }
                if(tipoSolicitanteArg == 3 || tipoSolicitanteArg == 1 || tipoSolicitanteArg == 4){
                    editarButton.setOnClickListener(){
                        val bundle = Bundle()
                        bundle.putInt("idPaquete", paquete.idPaquete)
                        view.findNavController().navigate(R.id.action_historialPaquetesFragment_to_editarEstadoDePaqueteFragment,bundle)
                    }

                }
                if(tipoSolicitanteArg == 2) {
                    editarButton.isEnabled = false
                }
                layout.addView(cardView)
            }
        }
        if (tipoSolicitanteArg == 2){
            viewLifecycleOwner.lifecycleScope.launch() {
                val listaPaquetes = cargarPaquetesResidente(idPrefilActual, token, apiService)!!
                renderizarDatos(listaPaquetes)
            }
        }
        else {
            viewLifecycleOwner.lifecycleScope.launch() {
                val listaPaquetes = cargarPaquetesConjunto(idConjunto, token, apiService)!!
                renderizarDatos(listaPaquetes)
            }
        }


                super.onViewCreated(view, savedInstanceState)
    }
    private suspend fun cargarPaquetesResidente(
        idPerfil: Int,
        token: String,
        apiService: ApiService
    ): List<Paquete>? {
        val listaPaquetes = apiService.obtenerPaquetesByUsuario(idPerfil, token).awaitResponse().body()
        return listaPaquetes
    }

    private suspend fun cargarPaquetesConjunto(
        idConjunto: Int,
        token: String,
        apiService: ApiService
    ): List<Paquete>? {
        val listaPaquetes = apiService.obtenerPaquetesByConjunto(idConjunto, token).awaitResponse().body()
        return listaPaquetes
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial_paquetes, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistorialPaquetesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistorialPaquetesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}