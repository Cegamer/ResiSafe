package com.resisafe.appconjuntos

import CustomAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VigilanteListaVisitantesFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_vigilante_lista_visitantes, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerUsuarios)
        val nombresTemporales = generarNombresTemporales()
        val adapter = CustomAdapter(nombresTemporales)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun generarNombresTemporales(): List<String> {

        return listOf("Juan Perez 12345678",
            "María López 87654321",
            "Carlos García 98765432")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VigilanteListaVisitantesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
