package com.resisafe.appconjuntos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment


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

        // Define las opciones del Spinner
        val complaintTypes = arrayOf(
            "Selecciona tu tipo de queja",
            "Demora en reservar",
            "Zona común en mal estado",
            "Problema de paquetería"
        )

        // Crea un ArrayAdapter para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, complaintTypes)

        // Establece el layout del dropdown del Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Obtiene una referencia al Spinner del layout
        val complaintTypeSpinner = view.findViewById<Spinner>(R.id.complaint_type_spinner)

        // Asigna el adapter al Spinner
        complaintTypeSpinner.adapter = adapter

        // Listener para el Spinner
        complaintTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Manejar la selección del usuario aquí
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Si no se selecciona nada, puedes manejarlo aquí
            }
        }
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
