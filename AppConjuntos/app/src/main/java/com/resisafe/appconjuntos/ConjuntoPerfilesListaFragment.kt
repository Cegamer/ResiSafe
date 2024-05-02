package com.resisafe.appconjuntos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appconjuntos.ui.AppMaster.Adapters.PerfilUsuario
import com.resisafe.appconjuntos.ui.AppMaster.Adapters.perfilUsuarioAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConjuntoPerfilesListaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConjuntoPerfilesListaFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_conjunto_perfiles_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = perfilUsuarioAdapter(mutableListOf(), mutableListOf())

        val textFiltro = view.findViewById<EditText>(R.id.filtroCedulaUsuario)
        val checkbox1 = view.findViewById<CheckBox>(R.id.checkBox)
        val checkbox2 = view.findViewById<CheckBox>(R.id.checkBox2)
        val checkbox3 = view.findViewById<CheckBox>(R.id.checkBox3)

        val checkbox4 = view.findViewById<CheckBox>(R.id.checkBox4)
        val checkbox5 = view.findViewById<CheckBox>(R.id.checkBox5)


        val args = arguments
        val idConjuntoArg = args?.getInt("idConjunto")

        if (args != null) {
            val idTipoPerfil = args.getInt("filtroInicial")

            when (idTipoPerfil) {
                1 -> checkbox3.isChecked = true;
                2 -> checkbox1.isChecked = true;
                3 -> checkbox2.isChecked = true;
            }
        }


        val botonNuevoPerfilConjunto = view.findViewById<Button>(R.id.botonNuevoUsuario)
        botonNuevoPerfilConjunto.setOnClickListener {
            val bundle = Bundle()
            if (idConjuntoArg != null) {
                bundle.putInt("idConjunto", idConjuntoArg)
            };
            view.findNavController().navigate(
                R.id.action_conjuntoPerfilesListaFragment_to_perfilCrearFragment,
                bundle
            )
        }

        fun aplicarFiltrado() {
            val tiposPerfilSeleccionados = mutableListOf<String>()
            if (checkbox1.isChecked) tiposPerfilSeleccionados.add("Residente")
            if (checkbox2.isChecked) tiposPerfilSeleccionados.add("Vigilante")
            if (checkbox3.isChecked) tiposPerfilSeleccionados.add("Administrador")

            val filtroActivo = when {
                checkbox4.isChecked -> 1
                checkbox5.isChecked -> 0
                else -> null
            }

            val datosFiltrados = adapter.getData().filter { usuario ->
                tiposPerfilSeleccionados.isEmpty() || usuario.tipoPerfil in tiposPerfilSeleccionados
            }.filter { usuario ->
                filtroActivo == null || usuario.activo == filtroActivo
            }.filter { usuario ->
                usuario.cedula.toString().contains(textFiltro.text)
            }

            adapter.actualizar(datosFiltrados.toMutableList())
        }

        // Agregar listeners para los checkboxes y el campo de filtro
        checkbox1.setOnCheckedChangeListener { _, _ -> aplicarFiltrado() }
        checkbox2.setOnCheckedChangeListener { _, _ -> aplicarFiltrado() }
        checkbox3.setOnCheckedChangeListener { _, _ -> aplicarFiltrado() }
        checkbox4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                checkbox5.isChecked = false
            aplicarFiltrado()
        }
        checkbox5.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                checkbox4.isChecked = false

            aplicarFiltrado()
        }
        textFiltro.addTextChangedListener { aplicarFiltrado() }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerVIew)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = adapter


        val apiService = RetrofitClient.apiService
        val context = this.requireContext()
        val token = ManejadorDeTokens.cargarTokenUsuario(context)?.token;

        if (token != null) {
            apiService.obtenerPerfilesConjunto(idConjuntoArg, token)
                .enqueue(object : Callback<List<PerfilUsuario>> {
                    override fun onResponse(
                        call: Call<List<PerfilUsuario>>,
                        response: Response<List<PerfilUsuario>>
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

                    override fun onFailure(call: Call<List<PerfilUsuario>>, t: Throwable) {
                        Log.e("Tag", "Response body is dsafadfafdasf")
                    }
                })
        }

        aplicarFiltrado();

        super.onViewCreated(view, savedInstanceState)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConjuntoPerfilesListaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConjuntoPerfilesListaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

