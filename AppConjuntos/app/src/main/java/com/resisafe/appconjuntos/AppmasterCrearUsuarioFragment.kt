package com.resisafe.appconjuntos

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppmasterCrearUsuarioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppmasterCrearUsuarioFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_appmaster_crear_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val registerButton: Button = view.findViewById(R.id.registerButton)
        var cedulaCampo: TextView = view.findViewById(R.id.cedulaField);
        var nombreCampo: TextView = view.findViewById(R.id.nombreField);
        var apellidoCampo: TextView = view.findViewById(R.id.apellidoField);
        var contrasenaCamo: TextView = view.findViewById(R.id.contrasenaField);

        registerButton.setOnClickListener() {


            val userData = UsuarioRegisterModel(
                nombreCampo.text.toString(),
                apellidoCampo.text.toString(),
                cedulaCampo.text.toString().toInt(),
                contrasenaCamo.text.toString()
            )

            val alerta: AlertDialog.Builder = AlertDialog.Builder(activity);
            alerta.setMessage("¿Está seguro de crear un usuario con los siguientes datos? \n${userData.cedula}\n${userData.nombre}\n${userData.apellido}\n${userData.contraseña}")
                .setCancelable(false).setPositiveButton("si") { dialog, wich ->

                    val apiService = RetrofitClient.apiService


                    apiService.registerUser(userData).enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    Log.d("Tag", responseBody.toString())
                                } else {
                                    Log.e("Tag", "Response body is null")
                                }
                            } else {
                                Log.e("Tag", "Unsuccessful response: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            Log.e("Tag", "Failed to make request: ${t.message}", t)
                        }
                    })
                }.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            val dialogo = alerta.create()
            dialogo.show()
        }


        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppmasterCrearUsuarioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppmasterCrearUsuarioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}