package com.resisafe.appconjuntos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appconjuntos.ui.AppMaster.Adapters.PerfilUsuario
import com.resisafe.appconjuntos.ui.AppMaster.Adapters.perfilUsuarioAdapter

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

        val datosDePrueba = listOf(
            PerfilUsuario(1, 123456789, "Juan Pérez", "Administrador", "Torre A", 101, 1),
            PerfilUsuario(2, 987654321, "María Rodríguez", "Residente", "Torre B", 202, 1),
            PerfilUsuario(3, 246813579, "Pedro González", "Residente", "Torre C", 303, 0),
            PerfilUsuario(4, 135792468, "Ana López", "Vigilante", "Torre D", 404, 0),
            PerfilUsuario(5, 864209731, "Carlos Martínez", "Residente", "Torre E", 505, 0),
            PerfilUsuario(6, 102938475, "Laura Sánchez", "Administrador", "Torre F", 606, 1),
            PerfilUsuario(7, 576829104, "Luis Hernández", "Vigilante", "Torre G", 707, 0),
            PerfilUsuario(8, 789012345, "Andrea Gómez", "Residente", "Torre H", 808, 0),
            PerfilUsuario(9, 219087654, "Sofía Ramírez", "Residente", "Torre I", 909, 0),
            PerfilUsuario(10, 345678901, "Diego Castillo", "Vigilante", "Torre J", 1010, 1),
            PerfilUsuario(11, 876543210, "Elena Torres", "Administrador", "Torre K", 1111, 1),
            PerfilUsuario(12, 987654123, "Javier Díaz", "Residente", "Torre L", 1212, 0),
            PerfilUsuario(13, 234567890, "Marta Ruiz", "Vigilante", "Torre M", 1313, 1),
            PerfilUsuario(14, 543210987, "Roberto Castro", "Administrador", "Torre N", 1414, 1),
            PerfilUsuario(15, 109876543, "Lucía Gutiérrez", "Residente", "Torre O", 1515, 1)
        )

        val datos = datosDePrueba

        val botonNuevoPerfilConjunto = view. findViewById<Button>(R.id.botonNuevoPerfilConjunto)

        val adapter = perfilUsuarioAdapter(datos)

        val textFiltro = view.findViewById<EditText>(R.id.filtroCedula)
        val checkbox1 = view.findViewById<CheckBox>(R.id.checkBox)
        val checkbox2 = view.findViewById<CheckBox>(R.id.checkBox2)
        val checkbox3 = view.findViewById<CheckBox>(R.id.checkBox3)

        val checkbox4 = view.findViewById<CheckBox>(R.id.checkBox4)
        val checkbox5 = view.findViewById<CheckBox>(R.id.checkBox5)


        val args = arguments
        val idConjuntoArg = args?.getInt("idConjunto")

        if (args != null) {
            val idTipoPerfil = args.getInt("filtroInicial")

            when(idTipoPerfil){
                1 -> checkbox3.isChecked = true;
                2 -> checkbox1.isChecked = true;
                3 -> checkbox2.isChecked = true;
            }
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

            val datosFiltrados = datosDePrueba.filter { usuario ->
                tiposPerfilSeleccionados.isEmpty() || usuario.tipoPerfil in tiposPerfilSeleccionados
            }.filter { usuario ->
                filtroActivo == null || usuario.activo == filtroActivo
            }.filter { usuario ->
                usuario.cedula.toString().contains(textFiltro.text)
            }

            adapter.actualizar(datosFiltrados)
        }

        // Agregar listeners para los checkboxes y el campo de filtro
        checkbox1.setOnCheckedChangeListener { _, _ -> aplicarFiltrado() }
        checkbox2.setOnCheckedChangeListener { _, _ -> aplicarFiltrado() }
        checkbox3.setOnCheckedChangeListener { _, _ -> aplicarFiltrado() }
        checkbox4.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                checkbox5.isChecked = false
            aplicarFiltrado()  }
        checkbox5.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                checkbox4.isChecked = false

            aplicarFiltrado() }
        textFiltro.addTextChangedListener { aplicarFiltrado() }

        aplicarFiltrado();
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerVIew)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = adapter

        botonNuevoPerfilConjunto.setOnClickListener{
            val bundle = Bundle()
            if (idConjuntoArg != null) {
                bundle.putInt("idConjunto", idConjuntoArg)
            };
            view.findNavController().navigate(R.id.action_nav_appmasterListaConjuntosFragment_to_conjuntoInfoFragment,bundle)
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

