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
 * Use the [ZonacomunInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ZonacomunInfoFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_zonacomun_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.actionBar?.title = "Información de Zona Común"

        val apiService = RetrofitClient.apiService
        val context = this.requireContext()
        val token = ManejadorDeTokens.cargarTokenUsuario(context)?.token!!;
        val args = arguments
        val botonListaReservas = view.findViewById<Button>(R.id.botonListaReservas)
        val buttonEliminar = view.findViewById<Button>(R.id.buttonEliminar)


        val idZonaComun = args?.getInt("idZonaComun")!!

        obtenerDatosZonaComun(idZonaComun, token, apiService, view)

        buttonEliminar.setOnClickListener(){
            eliminarZonaComun(idZonaComun, token, apiService, view)
        }

        botonListaReservas.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("idZonaComun", idZonaComun)
            view.findNavController().navigate(
                R.id.action_zonacomunInfoFragment_to_listaReservasFragment,
                bundle
            )
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun eliminarZonaComun(idZonaComun: Int, token: String, apiService: ApiService, view: View) {
        val builder = AlertDialog.Builder(view.context)
        builder.setMessage("¿Está Seguro de eliminar la zona común?\nEsta acción no se puede deshacer")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            apiService.deleteZonaComun(idZonaComun, token).enqueue(object :
                Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {}
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {}
            })

            dialog.dismiss()
            view.findNavController().popBackStack()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun obtenerDatosZonaComun(idZonaComun: Int, token: String, apiService: ApiService, view: View) {

        val idZonaComunText = view.findViewById<TextView>(R.id.idZonaComun)
        val nombreZonaComun = view.findViewById<TextView>(R.id.nombreZonaComun)
        val textoAforo = view.findViewById<TextView>(R.id.textoAforo)
        val textoPrecio = view.findViewById<TextView>(R.id.textoPrecio)
        val textoApertura = view.findViewById<TextView>(R.id.textoApertura)
        val textoCierre = view.findViewById<TextView>(R.id.textoCierre)
        val textoIntervaloMinutos = view.findViewById<TextView>(R.id.textoIntervaloMinutos)

        apiService.getZonaComunData(idZonaComun, token).enqueue(object :
            Callback<ZonaComun> {
            override fun onResponse(
                call: Call<ZonaComun>,
                response: Response<ZonaComun>
            ) {
                if (response.isSuccessful) {
                    val datos = response.body()!!
                    idZonaComunText.text = datos.idZonaComun.toString()
                    nombreZonaComun.text = datos.nombre
                    textoAforo.text = datos.aforoMaximo.toString()
                    textoPrecio.text = datos.precio.toString() + "$"
                    textoApertura.text = datos.horarioCierre.toString()
                    textoCierre.text = datos.horarioCierre.toString()
                    textoIntervaloMinutos.text = datos.intervaloTurnos.toString() + " Minutos"

                } else {
                    Log.e("Tag", "Response body is null")
                }
            }

            override fun onFailure(call: Call<ZonaComun>, t: Throwable) {
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
         * @return A new instance of fragment ZonacomunInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ZonacomunInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}