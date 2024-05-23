package com.resisafe.appresisafe

import android.os.Bundle
import android.util.Log
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
 * Use the [AdministradorHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdministradorHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(com.resisafe.appresisafe.ARG_PARAM1)
            param2 = it.getString(com.resisafe.appresisafe.ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val infoConjuntoCard = view.findViewById<CardView>(R.id.infoConjuntoCard)
        val vincularUsuariosCard = view.findViewById<CardView>(R.id.vincularUsuariosCard)
        val listaUsuariosCard = view.findViewById<CardView>(R.id.listaUsuariosCard)
        val cardHistorialPaquetes = view.findViewById<CardView>(R.id.cardHistorialPaquetes)
        val cardHistorialVisitantes = view.findViewById<CardView>(R.id.cardHistorialVisitantes)
        val cardZonasComunes = view.findViewById<CardView>(R.id.cardZonasComunes)
        val cardAgregarZonaComun = view.findViewById<CardView>(R.id.cardAgregarZonaComun)




        viewLifecycleOwner.lifecycleScope.launch() {

            val PerfilActual = ManejadorDeTokens.cargarPerfilActual(view.context)
            val bundle = Bundle()
            bundle.putInt("idConjunto", PerfilActual?.idConjunto!!)
            bundle.putInt("idPrefilActual",PerfilActual?.idPerfil!!)
            bundle.putInt("tipoUsuarioSolicitante", 1);

            infoConjuntoCard.setOnClickListener() {
                view.findNavController().navigate(
                    R.id.action_administradorHomeFragment_to_conjuntoInfoFragment2, bundle
                )

            }
            vincularUsuariosCard.setOnClickListener(){
                view.findNavController().navigate(
                    R.id.action_administradorHomeFragment_to_perfilCrearFragment3, bundle
                )

            }

            listaUsuariosCard.setOnClickListener(){
                view.findNavController().navigate(
                    R.id.action_administradorHomeFragment_to_conjuntoPerfilesListaFragment2, bundle
                )
            }


            cardZonasComunes.setOnClickListener(){
                view.findNavController().navigate(
                    R.id.action_administradorHomeFragment_to_fragmentConjuntoListaZonascomunes2, bundle
                )
            }


            cardAgregarZonaComun.setOnClickListener(){
                view.findNavController().navigate(
                    R.id.action_administradorHomeFragment_to_conjuntoAgregarZonacomunFragment3, bundle
                )
            }

            cardHistorialVisitantes.setOnClickListener(){
                view.findNavController().navigate(
                    R.id.action_administradorHomeFragment_to_vigilanteListaVisitantesFragment3, bundle
                )
            }

            cardHistorialPaquetes.setOnClickListener() {
                view.findNavController().navigate(
                    R.id.action_administradorHomeFragment_to_historialPaquetesFragment3,
                    bundle
                )
            }


        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_administrador_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdministradorHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            com.resisafe.appresisafe.AdministradorHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(com.resisafe.appresisafe.ARG_PARAM1, param1)
                    putString(com.resisafe.appresisafe.ARG_PARAM2, param2)
                }
            }
    }
}