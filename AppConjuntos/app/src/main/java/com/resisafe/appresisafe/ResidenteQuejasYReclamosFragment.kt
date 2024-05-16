package com.resisafe.appresisafe

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ResidenteQuejasYReclamosFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_residente_quejas_y_reclamos, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tiposQuejas: MutableMap<String, Int> = mutableMapOf()

        val complaintTypeSpinner = view.findViewById<Spinner>(R.id.complaint_type_spinner)
        val textoQueja = view.findViewById<EditText>(R.id.editTextTextMultiLine)

        val args = arguments
        val idConjuntoArg = args?.getInt("idConjunto")!!
        val idPrefilActual = args?.getInt("idPrefilActual")!!

        val botonEnviar = view.findViewById<Button>(R.id.enviar)
        val apiService = RetrofitClient.apiService
        val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token!!
        botonEnviar.setOnClickListener() {
            val datosQuejas = quejaReclamo(
                idquejasReclamos = 0,
                idTipo = tiposQuejas[complaintTypeSpinner.selectedItem.toString()]!!,
                quejaReclamo = textoQueja.text.toString(),
                idConjunto = idConjuntoArg,
                idPersonaQueEnvia = idPrefilActual
            )
            enviarQuejaReclamo(apiService, datosQuejas, token, view)
        }




        apiService.getTiposQUejas().enqueue(object : Callback<List<tiposQuejas>> {
            override fun onResponse(
                call: Call<List<tiposQuejas>>,
                response: Response<List<tiposQuejas>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        for (tipoQueja in responseBody) {
                            tiposQuejas[tipoQueja.nombreTipo] = tipoQueja.idtiposQuejasReclamos
                            Log.d("aa", tipoQueja.nombreTipo)
                        }
                        val arrayTiposQuejas: List<String> = tiposQuejas.keys.toList();

                        val adapter =
                            ArrayAdapter(requireContext(), R.layout.spinner_box, arrayTiposQuejas)
                        adapter.setDropDownViewResource(R.layout.spinner_item)
                        complaintTypeSpinner.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<List<tiposQuejas>>, t: Throwable) {
                Log.e("Tag", "Failed to make request: ${t.message}", t)
            }
        })

    }


    fun enviarQuejaReclamo(
        apiService: ApiService,
        quejaReclamo: quejaReclamo,
        token: String,
        view: View
    ) {
        apiService.postQuejasReclamos(quejaReclamo, token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val builder = AlertDialog.Builder(view.context)
                        builder.setMessage("Enviado correctamente")
                        builder.setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()
                            view.findNavController().popBackStack()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                } else {
                    Log.e("FALLO", quejaReclamo.toString())

                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Tag", "Failed to make request: ${t.message}", t)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResidenteQuejasYReclamosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
