package com.resisafe.appconjuntos.ui.AppMaster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.resisafe.appconjuntos.ApiResponse
import com.resisafe.appconjuntos.Conjunto
import com.resisafe.appconjuntos.ManejadorDeTokens
import com.resisafe.appconjuntos.R
import com.resisafe.appconjuntos.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppmasterRegistrarConjuntoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppmasterRegistrarConjuntoFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_appmaster_registrar_conjunto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val boton: Button = view.findViewById(R.id.buttonCrearConjunto)
        val buttonCancelar : Button = view.findViewById(R.id.buttonCancelar)
        buttonCancelar.setOnClickListener {
            view.findNavController().popBackStack()

        }

        boton.setOnClickListener() {

            val tokenResponse = ManejadorDeTokens.cargarTokenUsuario(this.requireContext())
            val apiService = RetrofitClient.apiService
            val context = this.requireContext()

            val nombreCampo: TextInputEditText = view!!.findViewById(R.id.nombreConjuntoInput)
            val direccionCampo: TextInputEditText = view!!.findViewById(R.id.direccionConjuntoInput)



            val conjuntoData = Conjunto(
                0,
                nombreCampo.text.toString(),
                direccionCampo.text.toString(),
                1
            )

            if (tokenResponse != null) {
                Log.d("Tag", "Token: ${tokenResponse.token}, UserID: ${tokenResponse.userID}")
                apiService.crearConjunto(conjuntoData, tokenResponse.token)
                    .enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>
                        ) {
                            if (response.isSuccessful) {
                                val datos = response.body()!!
                            } else {
                                Log.e("Tag", "Response body is null")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            Log.e("Tag", "Response body is dsafadfafdasf")
                        }
                    })
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppmasterRegistrarConjuntoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppmasterRegistrarConjuntoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}