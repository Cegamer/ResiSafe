package com.resisafe.appconjuntos.ui.AppMaster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.resisafe.appconjuntos.CardItem
import com.resisafe.appconjuntos.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppmasterHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppmasterHomeFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_appmaster_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val registrarConjuntosCard = view.findViewById<CardView>(R.id.registrarConjuntosCard)
        val listaConjuntosCard = view.findViewById<CardView>(R.id.listaConjuntosCard)
        val vincularUsuariosCard = view.findViewById<CardView>(R.id.vincularUsuariosCard)
        val crearUsuariosCard = view.findViewById<CardView>(R.id.crearUsuariosCard)
        val listaUsuariosCard = view.findViewById<CardView>(R.id.listaUsuariosCard)

        registrarConjuntosCard.setOnClickListener {
            view.findNavController().navigate(
                R.id.action_nav_home_to_nav_registrar_conjunto
            )
        }
        listaConjuntosCard.setOnClickListener {
            view.findNavController().navigate(
                R.id.action_nav_home_to_nav_appmasterListaConjuntosFragment
            )
        }
        vincularUsuariosCard.setOnClickListener {
            view.findNavController().navigate(
                R.id.action_nav_home_to_perfilCrearFragment
            )
        }
        listaUsuariosCard.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_nav_home_to_appmasterListaUsuariosFragment)
        }
        crearUsuariosCard.setOnClickListener {
            view.findNavController().navigate(
                R.id.action_nav_home_to_appmasterCrearUsuarioFragment
            )
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
         * @return A new instance of fragment AppmasterHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppmasterHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}