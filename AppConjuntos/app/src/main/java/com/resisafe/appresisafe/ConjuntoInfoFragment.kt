package com.resisafe.appresisafe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
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
 * Use the [ConjuntoInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConjuntoInfoFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_conjunto_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tokenResponse = ManejadorDeTokens.cargarTokenUsuario(view.context)
        val apiService = RetrofitClient.apiService

        val botonResidentes = view.findViewById<Button>(R.id.botonResidentes)
        val botonVigilantes = view.findViewById<Button>(R.id.botonVigilantes)
        val botonAdmins = view.findViewById<Button>(R.id.botonAdmins)

        val buttonAgregarZonaComun = view.findViewById<Button>(R.id.buttonAgregarZonaComun)
        val botonListaZonaComun = view.findViewById<Button>(R.id.botonListaZonaComun)

        val args = arguments
        if (args != null) {
            val idConjuntoArg = args.getInt("idConjunto")
            if (tokenResponse != null) {
                apiService.obtenerInfoConjunto(idConjuntoArg, tokenResponse.token)
                    .enqueue(object : Callback<Conjunto> {
                        override fun onResponse(
                            call: Call<Conjunto>,
                            response: Response<Conjunto>
                        ) {
                            if (response.isSuccessful) {
                                val conjunto = response.body()

                                val idConjunto = view.findViewById<TextView>(R.id.idConjunto)
                                val nombreConjunto =
                                    view.findViewById<TextView>(R.id.nombreConjunto)
                                val direccion = view.findViewById<TextView>(R.id.direccion)
                                val activo = view.findViewById<Switch>(R.id.switch1)

                                if (conjunto != null) {
                                    idConjunto.text = conjunto.idConjunto.toString()
                                    nombreConjunto.text = conjunto.nombre
                                    direccion.text = conjunto.direccion
                                    activo.isChecked = conjunto.activo == 1;
                                }
                                val bundle = Bundle()
                                bundle.putInt("idConjunto", idConjuntoArg);

                                botonResidentes.setOnClickListener() {
                                    bundle.putInt("filtroInicial", 2);
                                    view.findNavController().navigate(
                                        R.id.action_conjuntoInfoFragment_to_conjuntoPerfilesListaFragment,
                                        bundle
                                    )
                                }
                                botonVigilantes.setOnClickListener() {
                                    bundle.putInt("filtroInicial", 3);
                                    view.findNavController().navigate(
                                        R.id.action_conjuntoInfoFragment_to_conjuntoPerfilesListaFragment,
                                        bundle
                                    )
                                }
                                botonAdmins.setOnClickListener() {
                                    bundle.putInt("filtroInicial", 1);
                                    view.findNavController().navigate(
                                        R.id.action_conjuntoInfoFragment_to_conjuntoPerfilesListaFragment,
                                        bundle
                                    )
                                }

                                buttonAgregarZonaComun.setOnClickListener {
                                    bundle.putInt("idConjunto", idConjuntoArg)
                                    view.findNavController().navigate(
                                        R.id.action_conjuntoInfoFragment_to_conjuntoAgregarZonacomunFragment2,
                                        bundle
                                    )
                                }

                                botonListaZonaComun.setOnClickListener{
                                    bundle.putInt("idConjunto", idConjuntoArg)
                                    view.findNavController().navigate(R.id.action_conjuntoInfoFragment_to_fragmentConjuntoListaZonascomunes,bundle)

                                }


                            } else {
                                Log.e("Tag", "Response body is null")

                            }
                        }

                        override fun onFailure(call: Call<Conjunto>, t: Throwable) {
                            Log.e("Tag", "FalloPeticion")

                        }
                    })
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConjuntoInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConjuntoInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}