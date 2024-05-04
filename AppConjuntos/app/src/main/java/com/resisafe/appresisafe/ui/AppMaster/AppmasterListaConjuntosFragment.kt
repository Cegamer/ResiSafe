package com.resisafe.appresisafe.ui.AppMaster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.resisafe.appresisafe.Conjunto
import com.resisafe.appresisafe.R
import com.resisafe.appresisafe.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppmasterListaConjuntosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppmasterListaConjuntosFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_appmaster_lista_conjuntos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService = RetrofitClient.apiService
        val context = this.requireContext()
        activity?.actionBar?.title = "Conjuntos Registrados"


        val boton = view.findViewById<Button>(R.id.buttonNuevoConjunto)
        boton.setOnClickListener() {
            view.findNavController()
                .navigate(R.id.action_nav_appmasterListaConjuntosFragment_to_nav_registrar_conjunto)
        }

        val layout = view.findViewById<LinearLayout>(R.id.layoutListaConjuntos)



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
        })


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppmasterListaConjuntosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppmasterListaConjuntosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}