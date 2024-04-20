package com.resisafe.appconjuntos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appconjuntos.ui.AppMaster.Adapters.UsuarioAdapter
import com.resisafe.appconjuntos.ui.AppMaster.Adapters.perfilUsuarioAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AppmasterListaUsuariosFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_appmaster_lista_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val botonNuevoUsuario = view.findViewById<Button>(R.id.botonNuevoUsuario)

        botonNuevoUsuario.setOnClickListener {
            view.findNavController().navigate(
                R.id.action_appmasterListaUsuariosFragment_to_appmasterCrearUsuarioFragment
            )
        }

        val textFiltro = view.findViewById<EditText>(R.id.filtroCedulaUsuario)

        val Usuarios: MutableList<UserData> = mutableListOf();
        val adapter = UsuarioAdapter(Usuarios,Usuarios)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = adapter

        fun aplicarFiltrado() {
            val datosFiltrados = adapter.getData().filter { usuarios ->
                usuarios.cedula.toString().contains(textFiltro.text)
            }
            adapter.actualizar(datosFiltrados.toMutableList())
        }
        textFiltro.addTextChangedListener { aplicarFiltrado() }


        val apiService = RetrofitClient.apiService
        val context = this.requireContext()
        val token = ManejadorDeTokens.cargarTokenUsuario(context)?.token;

        if (token != null) {
            apiService.getUsers(token).enqueue(object : Callback<List<UserData>> {
                override fun onResponse(
                    call: Call<List<UserData>>,
                    response: Response<List<UserData>>
                ) {
                    if (response.isSuccessful) {
                        val datos = response.body()
                        if (datos != null) {
                            adapter.generarListaOriginal(datos.toMutableList())
                        }
                    } else {
                        Log.e("Tag", "Response body is null")
                    }
                }

                override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                    Log.e("Tag", "Response body is dsafadfafdasf")
                }
            })
        }



        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppmasterListaUsuariosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}