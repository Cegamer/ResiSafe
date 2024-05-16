package com.resisafe.appresisafe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VigilanteHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VigilanteHomeFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_vigilante_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewLifecycleOwner.lifecycleScope.launch() {

            val PerfilActual = ManejadorDeTokens.cargarPerfilActual(view.context)

            val bundle = Bundle()
            bundle.putInt("idConjunto", PerfilActual?.idConjunto!!)
            bundle.putInt("idPrefilActual",PerfilActual?.idPerfil!!)

            val registroVisitantesBinding = view.findViewById<CardView>(R.id.registrovisitantes)
            registroVisitantesBinding.setOnClickListener {
                view.findNavController().navigate(
                    R.id.action_vigilanteHomeFragment_to_vigilanteRegistroVisitanteFragment
                )
            }
            val historialvisitas = view.findViewById<CardView>(R.id.historialvisitas)
            historialvisitas.setOnClickListener {
                view.findNavController().navigate(
                    R.id.action_vigilanteHomeFragment_to_vigilanteListaVisitantesFragment,bundle
                )
            }
            val registrarvisita = view.findViewById<CardView>(R.id.registrarvisita)
            registrarvisita.setOnClickListener {

                view.findNavController().navigate(
                    R.id.action_vigilanteHomeFragment_to_vigilanteRegistroVisitaFragment, bundle
                )
            }

            val registrarPaquete = view.findViewById<CardView>(R.id.registropaqueteria)
            registrarPaquete.setOnClickListener(){
                view.findNavController().navigate(R.id.action_vigilanteHomeFragment_to_vigilanteRegistrarPaqueteFragment,bundle)
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
         * @return A new instance of fragment VigilanteHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VigilanteHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}