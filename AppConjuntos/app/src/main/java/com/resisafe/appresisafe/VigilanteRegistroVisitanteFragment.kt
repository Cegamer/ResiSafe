package com.resisafe.appresisafe

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VigilanteRegistroVisitanteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VigilanteRegistroVisitanteFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_vigilante_registro_visitante, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.title = "Registrar Visitante"

        val cedulaField = view.findViewById<TextInputEditText>(R.id.cedulaField)
        val nombreField = view.findViewById<TextInputEditText>(R.id.nombreField)
        val apellidoField = view.findViewById<TextInputEditText>(R.id.apellidoField)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val apiService = RetrofitClient.apiService
        val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token!!



        cancelButton.setOnClickListener(){
            view.findNavController().popBackStack()
        }

        registerButton.setOnClickListener(){
            val visitante = visitante(
                idVisitante = 0,
                nombre = nombreField.text.toString(),
                apellido = apellidoField.text.toString(),
                cedula = cedulaField.text.toString().toInt(),
                foto = ""
            )
            apiService.registrarVisitante(visitante,token).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val builder = AlertDialog.Builder(view.context)
                            builder.setMessage("Visitante Registrado correctamente")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                                view.findNavController().popBackStack()
                            }
                            val dialog = builder.create()
                            dialog.show()
                        }
                    }
                }
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("Tag", "Failed to make request: ${t.message}", t)
                }
            })
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VigilanteRegistroVisitanteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VigilanteRegistroVisitanteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}