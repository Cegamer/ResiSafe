package com.resisafe.appresisafe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ZonacomunInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ZonacomunInfoFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_zonacomun_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val apiService = RetrofitClient.apiService
        val context = this.requireContext()
        val token = ManejadorDeTokens.cargarTokenUsuario(context)?.token;
        val args = arguments
        val botonListaReservas = view.findViewById<Button>(R.id.botonListaReservas)

        if(args != null){
            val idZonaComunArg = args.getInt("idZonaComun")

            botonListaReservas.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("idZonaComun", idZonaComunArg)
                view.findNavController().navigate(
                    R.id.action_zonacomunInfoFragment_to_listaReservasFragment,
                    bundle
                )
            }

            /*
            if (token != null) {
                apiService.obtenerConjuntos().enqueue(object :
                    Callback<List<Conjunto>> {
                    override fun onResponse(
                        call: Call<List<Conjunto>>,
                        response: Response<List<Conjunto>>
                    ) {
                        if (response.isSuccessful) {
                            val datos = response.body()!!

                            for (conjunto in datos) {
                                val cardView = LayoutInflater.from(requireContext())
                                    .inflate(R.layout.itemconjuntos, null) as CardView
                                val textViewId = cardView.findViewById<TextView>(R.id.conjuntoId)
                                val textViewNombre = cardView.findViewById<TextView>(R.id.conjuntoNombre)
                                val textViewDireccion: TextView =
                                    cardView.findViewById(R.id.conjuntoDireccion)
                                val boton = cardView.findViewById<ImageButton>(R.id.imageButton)


                                textViewId.text = conjunto.idConjunto.toString()
                                textViewNombre.text = conjunto.nombre
                                textViewDireccion.text = conjunto.direccion

                                layout.addView(cardView)
                                boton.setOnClickListener() {
                                    val bundle = Bundle()
                                    bundle.putInt("idConjunto", conjunto.idConjunto);
                                    view.findNavController().navigate(
                                        R.id.action_nav_appmasterListaConjuntosFragment_to_conjuntoInfoFragment,
                                        bundle
                                    )
                                }


                            }
                        } else {
                            Log.e("Tag", "Response body is null")
                        }
                    }

                    override fun onFailure(call: Call<List<Conjunto>>, t: Throwable) {
                        Log.e("Tag", "Response body is dsafadfafdasf")
                    }
                })*/
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
         * @return A new instance of fragment ZonacomunInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ZonacomunInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}