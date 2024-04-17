package com.resisafe.appconjuntos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import com.resisafe.appconjuntos.RetrofitClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilCrearFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilCrearFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_perfil_crear, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spinnerConjunto: Spinner = view.findViewById(R.id.spinnerConjunto)

        val mapConjuntos: MutableMap<String, Int> = mutableMapOf()
        apiService.obtenerConjuntos().enqueue(object : Callback<List<Conjunto>> {
            override fun onResponse(
                call: Call<List<Conjunto>>,
                response: Response<List<Conjunto>>
            ) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    if (datos != null) {
                        datos.filter { it.idConjunto != 1 }.forEach { conjunto ->
                            mapConjuntos[conjunto.nombre] = conjunto.idConjunto
                        }
                        val nombresConjuntos = mapConjuntos.keys.toList()
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresConjuntos)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerConjunto.adapter = adapter
                    } else {
                        Log.e("Tag", "Response body is null")
                    }
                } else {
                    Log.e("Tag", "Response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<Conjunto>>, t: Throwable) {
                Log.e("Tag", "Error en la solicitud: ${t.message}")
            }
        })

        spinnerConjunto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val nombreConjunto = parent.getItemAtPosition(position).toString()
                val idConjunto = mapConjuntos[nombreConjunto]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        val cedulaBuscador = view.findViewById<EditText>(R.id.cedulaBuscador)
        val tokenResponse = ManejadorDeTokens.cargarTokenUsuario(this.requireContext())

        val idText = view.findViewById<TextView>(R.id.idUsuarioText)
        val nombreText = view.findViewById<TextView>(R.id.textView11)

        if (tokenResponse != null) {
            Log.d("Tag", "Token: ${tokenResponse.token}, UserID: ${tokenResponse.userID}")
            cedulaBuscador.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    apiService.getUserByCedula(cedulaBuscador.text.toString().toInt(), tokenResponse.token).enqueue(object : Callback<UserData> {
                        override fun onResponse(
                            call: Call<UserData>,
                            response: Response<UserData>
                        ) {
                            if (response.isSuccessful) {
                                val datos = response.body()

                                if (datos != null) {
                                    idText.text = datos.idUsuario.toString()
                                    nombreText.text = datos.nombre +" "+datos.apellido
                                }
                            } else {
                                Log.e("Tag", "Response unsuccessful")
                            }
                        }
                        override fun onFailure(call: Call<UserData>, t: Throwable) {
                            Log.e("Tag", "Error en la solicitud: ${t.message}")
                        }
                    })
                    return@OnEditorActionListener true
                }
                false
            })
        }

        /*===============================================================================*/

        val spinnerTipoPerfil: Spinner = view.findViewById(R.id.spinnerTipoPerfil)

        val mapTiposPerfil: MutableMap<String, Int> = mutableMapOf()
        apiService.getTiposPerfil().enqueue(object : Callback<List<TipoPerfil>> {
            override fun onResponse(
                call: Call<List<TipoPerfil>>,
                response: Response<List<TipoPerfil>>
            ) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    if (datos != null) {
                        for (tipo in datos){
                            mapTiposPerfil[tipo.nombreTipo] = tipo.idTipo
                        }

                        val nombresTipos = mapTiposPerfil.keys.toList()
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresTipos)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerTipoPerfil.adapter = adapter
                    } else {
                        Log.e("Tag", "Response body is null")
                    }
                } else {
                    Log.e("Tag", "Response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<TipoPerfil>>, t: Throwable) {
                Log.e("Tag", "Error en la solicitud: ${t.message}")
            }
        })

        spinnerTipoPerfil.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val nombrePerfil = parent.getItemAtPosition(position).toString()
                val idPerfil = mapTiposPerfil[nombrePerfil]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        val buttonCrearPerfil = view.findViewById<Button>(R.id.buttonCrearPerfil)

        buttonCrearPerfil.setOnClickListener{
            for ((key, value) in mapTiposPerfil) {
                Log.d("Mapa", "Clave: $key, Valor: $value")
            }
            val idUsuario: Int = idText.text.toString().toIntOrNull() ?: 0
            val idConjunto: Int? = mapConjuntos[spinnerConjunto.selectedItem?.toString()]
            val idTipoPerfil: Int? = mapTiposPerfil[spinnerTipoPerfil.selectedItem?.toString()]

            val profileData: ProfileData = ProfileData(0, idUsuario, idConjunto ?: 0, idTipoPerfil ?: 0, 1)

            if (tokenResponse != null) {
                Log.d("WEAAAAAAAAA ", tokenResponse.token)
                Log.d("Perfil", "${profileData.IdPerfil} -${profileData.IdUsuario} - ${profileData.IdConjunto} - ${profileData.IdTipoPerfil} - ${profileData.Activo}")

                apiService.createProfile(profileData,tokenResponse.token).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {

                        if (response.isSuccessful) {
                            val datos = response.body()

                        } else {
                            Log.e("Tag", "Response unsuccessful")
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Log.e("Tag", "Error en la solicitud: ${t.message}")
                    }
                })
            }

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
         * @return A new instance of fragment PerfilCrearFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilCrearFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}