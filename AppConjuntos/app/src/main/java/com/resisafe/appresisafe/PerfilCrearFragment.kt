package com.resisafe.appresisafe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.resisafe.appresisafe.RetrofitClient.apiService
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

        //Definir datos spinner conjuntos
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
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.spinner_box,
                            nombresConjuntos
                        )
                        adapter.setDropDownViewResource(R.layout.spinner_item)
                        spinnerConjunto.adapter = adapter
                        val args = arguments
                        if(args != null) {
                            val idConjunto = args.getInt("idConjunto")
                            val tipoUsuarioSolicitante = args.getInt("tipoUsuarioSolicitante")

                            if(tipoUsuarioSolicitante == 1){
                                val conjuntoActual = datos.find { it.idConjunto == idConjunto }
                                spinnerConjunto.setSelection(nombresConjuntos.indexOf(conjuntoActual?.nombre))
                                spinnerConjunto.isEnabled = false
                            }
                        }
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
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val nombreConjunto = parent.getItemAtPosition(position).toString()
                val idConjunto = mapConjuntos[nombreConjunto]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        //Buscador por cedula
        val cedulaBuscador = view.findViewById<EditText>(R.id.cedulaBuscador)
        val tokenResponse = ManejadorDeTokens.cargarTokenUsuario(this.requireContext())
        val idText = view.findViewById<TextView>(R.id.idUsuarioText)
        val nombreText = view.findViewById<TextView>(R.id.textView11)
        fun cargarUsuarioPorCedula(){
            if (tokenResponse != null) {
                apiService.getUserByCedula(
                    cedulaBuscador.text.toString().toInt(),
                    tokenResponse.token
                ).enqueue(object : Callback<UserData> {
                    override fun onResponse(
                        call: Call<UserData>,
                        response: Response<UserData>
                    ) {
                        if (response.isSuccessful) {
                            val datos = response.body()

                            if (datos != null) {
                                idText.text = datos.idUsuario.toString()
                                nombreText.text = datos.nombre + " " + datos.apellido
                            }
                        } else {
                            Log.e("Tag", "Response unsuccessful")
                        }
                    }

                    override fun onFailure(call: Call<UserData>, t: Throwable) {
                        Log.e("Tag", "Error en la solicitud: ${t.message}")
                    }
                })
            }

        }
        if (tokenResponse != null) {
            Log.d("Tag", "Token: ${tokenResponse.token}, UserID: ${tokenResponse.userID}")
            cedulaBuscador.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    cargarUsuarioPorCedula()
                    return@OnEditorActionListener true
                }
                false
            })
        }

        ///Spinner tipo perfil/
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
                        for (tipo in datos) {
                            mapTiposPerfil[tipo.nombreTipo] = tipo.idTipo
                        }

                        val nombresTipos = mapTiposPerfil.keys.toList()
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.spinner_box,
                            nombresTipos
                        )
                        adapter.setDropDownViewResource(R.layout.spinner_item)
                        spinnerTipoPerfil.adapter = adapter

                        val args = arguments
                        if(args != null) {
                            val idConjunto = args.getInt("idConjunto")
                            val tipoUsuarioSolicitante = args.getInt("tipoUsuarioSolicitante")

                            if(tipoUsuarioSolicitante == 0){
                                spinnerTipoPerfil.setSelection(nombresTipos.indexOf("Residente"))
                                spinnerTipoPerfil.isEnabled = false

                                val dataToken = ManejadorDeTokens.cargarTokenUsuario(view.context)
                                val token = dataToken?.token;
                                val idUsuario = dataToken?.userID

                                if (token != null && idUsuario != null) {
                                    apiService.getUserData(idUsuario,token).enqueue(object : Callback<UserData> {
                                        override fun onResponse(
                                            call: Call<UserData>,
                                            response: Response<UserData>
                                        ) {
                                            if (response.isSuccessful) {
                                                val data = response.body()
                                                if (data != null) {
                                                    cedulaBuscador.setText( data.cedula.toString())
                                                    cargarUsuarioPorCedula()
                                                    cedulaBuscador.isEnabled = false;
                                                };
                                            } else {
                                                Log.e("Tag", "Response body is null")
                                            }
                                        }
                                        override fun onFailure(call: Call<UserData>, t: Throwable) {
                                            Log.e("Tag", "Response body is dsafadfafdasf")
                                        }
                                    })
                                }
                            }
                            else {
                                val adapter = ArrayAdapter(
                                    requireContext(),
                                    R.layout.spinner_box,
                                    nombresTipos.filter {it !=  "AppMaster"}
                                )
                                adapter.setDropDownViewResource(R.layout.spinner_item)
                                spinnerTipoPerfil.adapter = adapter
                            }

                        }
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
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val nombrePerfil = parent.getItemAtPosition(position).toString()
                val idPerfil = mapTiposPerfil[nombrePerfil]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        //Botón crear perfil
        val buttonCrearPerfil = view.findViewById<Button>(R.id.buttonCrearPerfil)
        buttonCrearPerfil.setOnClickListener {
            for ((key, value) in mapTiposPerfil) {
                Log.d("Mapa", "Clave: $key, Valor: $value")
            }
            val idUsuario: Int = idText.text.toString().toIntOrNull() ?: 0
            val idConjunto: Int? = mapConjuntos[spinnerConjunto.selectedItem?.toString()]
            val idTipoPerfil: Int? = mapTiposPerfil[spinnerTipoPerfil.selectedItem?.toString()]

            val profileData: ProfileData =
                ProfileData(0, idUsuario, idConjunto ?: 0, idTipoPerfil ?: 0, 1)

            if (tokenResponse != null) {
                Log.d("WEAAAAAAAAA ", tokenResponse.token)
                Log.d(
                    "Perfil",
                    "${profileData.IdPerfil} -${profileData.IdUsuario} - ${profileData.IdConjunto} - ${profileData.IdTipoPerfil} - ${profileData.Activo}"
                )

                apiService.createProfile(profileData, tokenResponse.token)
                    .enqueue(object : Callback<ApiResponse> {
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