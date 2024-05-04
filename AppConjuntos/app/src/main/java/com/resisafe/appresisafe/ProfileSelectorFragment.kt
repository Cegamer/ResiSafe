package com.resisafe.appresisafe

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import com.google.gson.Gson
import com.resisafe.appresisafe.ui.AppMaster.AppMasterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileSelectorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileSelectorFragment : Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_profile_selector, container, false)
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardAddProfile: CardView = view.findViewById(R.id.cardAddProfile)
        val gridLayout: GridLayout = view.findViewById(R.id.profilesGrid)

        val tokenResponse = ManejadorDeTokens.cargarTokenUsuario(this.requireContext())
        val apiService = RetrofitClient.apiService
        val context = this.requireContext()

        if (tokenResponse != null) {
            Log.d("Tag", "Token: ${tokenResponse.token}, UserID: ${tokenResponse.userID}")

            apiService.obtenerDatosPerfiles(tokenResponse.userID, tokenResponse.token)
                .enqueue(object :
                    Callback<List<CardItem>> {
                    override fun onResponse(
                        call: Call<List<CardItem>>,
                        response: Response<List<CardItem>>
                    ) {
                        if (response.isSuccessful) {
                            val datos = response.body()!!

                            for (perfil in datos) {
                                val cardView = LayoutInflater.from(requireContext())
                                    .inflate(R.layout.itemperfiles, gridLayout, false) as CardView
                                val textViewConjunto =
                                    cardView.findViewById<TextView>(R.id.ConuntoText)
                                val textViewTipoPerfil =
                                    cardView.findViewById<TextView>(R.id.conjuntoId)
                                val idPerfil: TextView = cardView.findViewById(R.id.idPerfil)

                                val ProfileIcon: ImageView = cardView.findViewById(R.id.ProfileIcon)


                                textViewConjunto.text = perfil.nombreConjunto
                                textViewTipoPerfil.text = perfil.nombreTipoPerfil
                                idPerfil.text = perfil.idPerfil.toString()


                                gridLayout.addView(cardView)
                                when (perfil.nombreTipoPerfil) {

                                    //Administrador
                                    "Administrador" -> {

                                        ProfileIcon.setImageResource(R.drawable.manage_accounts)
                                        cardView.setOnClickListener {
                                            val apiService = RetrofitClient.apiService
                                            val idText: TextView =
                                                cardView.findViewById(R.id.idPerfil)
                                            val idPerfil = idText.text.toString().toInt()
                                            val tokenResponse =
                                                ManejadorDeTokens.cargarTokenUsuario(context)

                                            if (tokenResponse != null) {
                                                apiService.loginProfile(
                                                    idPerfil,
                                                    tokenResponse.token
                                                ).enqueue(object : Callback<LoginResponse> {
                                                    override fun onResponse(
                                                        call: Call<LoginResponse>,
                                                        response: Response<LoginResponse>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val responseBody = response.body()
                                                            if (responseBody != null) {
                                                                val token = responseBody.token
                                                                val userId = responseBody.userID
                                                                ManejadorDeTokens.guardarTokenUsuario(
                                                                    context,
                                                                    responseBody
                                                                )
                                                                val intent = Intent(
                                                                    context,
                                                                    com.resisafe.appresisafe.AdministradorActivity::class.java
                                                                )
                                                                val b = Bundle()
                                                                b.putInt("idPerfil",idPerfil) //Your id

                                                                intent.putExtras(b)
                                                                startActivity(intent)
                                                                activity?.finish()

                                                            } else {
                                                                Log.e(
                                                                    "Tag",
                                                                    "Response body is null"
                                                                )
                                                            }
                                                        } else {
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<LoginResponse>,
                                                        t: Throwable
                                                    ) {
                                                    }
                                                })
                                            }
                                        }
                                    }

                                    //Administrador
                                    "Residente" -> {
                                        cardView.setOnClickListener {
                                            val apiService = RetrofitClient.apiService
                                            val idText: TextView =
                                                cardView.findViewById(R.id.idPerfil)
                                            val idPerfil = idText.text.toString().toInt()
                                            val tokenResponse =
                                                ManejadorDeTokens.cargarTokenUsuario(context)

                                            if (tokenResponse != null) {
                                                apiService.loginProfile(
                                                    idPerfil,
                                                    tokenResponse.token
                                                ).enqueue(object : Callback<LoginResponse> {
                                                    override fun onResponse(
                                                        call: Call<LoginResponse>,
                                                        response: Response<LoginResponse>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val responseBody = response.body()
                                                            if (responseBody != null) {
                                                                val token = responseBody.token
                                                                val userId = responseBody.userID
                                                                ManejadorDeTokens.guardarTokenUsuario(
                                                                    context,
                                                                    responseBody
                                                                )
                                                                val intent = Intent(
                                                                    context,
                                                                    ResidenteActivity::class.java
                                                                )
                                                                startActivity(intent)

                                                            } else {
                                                                Log.e(
                                                                    "Tag",
                                                                    "Response body is null"
                                                                )
                                                            }
                                                        } else {
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<LoginResponse>,
                                                        t: Throwable
                                                    ) {
                                                    }
                                                })
                                            }
                                        }
                                    }

                                    //Vigilante
                                    "Vigilante" -> {
                                        ProfileIcon.setImageResource(R.drawable.shield_person)
                                        cardView.setOnClickListener {
                                            val apiService = RetrofitClient.apiService
                                            val idText: TextView =
                                                cardView.findViewById(R.id.idPerfil)
                                            val idPerfil = idText.text.toString().toInt()
                                            val tokenResponse =
                                                ManejadorDeTokens.cargarTokenUsuario(context)

                                            if (tokenResponse != null) {
                                                apiService.loginProfile(
                                                    idPerfil,
                                                    tokenResponse.token
                                                ).enqueue(object : Callback<LoginResponse> {
                                                    override fun onResponse(
                                                        call: Call<LoginResponse>,
                                                        response: Response<LoginResponse>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val responseBody = response.body()
                                                            if (responseBody != null) {
                                                                val token = responseBody.token
                                                                val userId = responseBody.userID
                                                                ManejadorDeTokens.guardarTokenUsuario(
                                                                    context,
                                                                    responseBody
                                                                )
                                                                val intent = Intent(
                                                                    context,
                                                                    VigilanteActivity::class.java
                                                                )
                                                                startActivity(intent)

                                                            } else {
                                                                Log.e(
                                                                    "Tag",
                                                                    "Response body is null"
                                                                )
                                                            }
                                                        } else {
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<LoginResponse>,
                                                        t: Throwable
                                                    ) {
                                                    }
                                                })
                                            }
                                        }
                                    }

                                    //AppMaster
                                    "AppMaster" -> {
                                        ProfileIcon.setImageResource(R.drawable.egineering)
                                        cardView.setOnClickListener {
                                            val apiService = RetrofitClient.apiService
                                            val idText: TextView =
                                                cardView.findViewById(R.id.idPerfil)
                                            val idPerfil = idText.text.toString().toInt()
                                            val tokenResponse =
                                                ManejadorDeTokens.cargarTokenUsuario(context)

                                            if (tokenResponse != null) {
                                                apiService.loginProfile(
                                                    idPerfil,
                                                    tokenResponse.token
                                                ).enqueue(object : Callback<LoginResponse> {
                                                    override fun onResponse(
                                                        call: Call<LoginResponse>,
                                                        response: Response<LoginResponse>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val responseBody = response.body()
                                                            if (responseBody != null) {
                                                                val token = responseBody.token
                                                                val userId = responseBody.userID
                                                                Log.d(
                                                                    "Tag",
                                                                    "Token: $token, UserID: $userId"
                                                                )
                                                                ManejadorDeTokens.guardarTokenUsuario(
                                                                    context,
                                                                    responseBody
                                                                )
                                                                Log.d("Tag", "Token Guardado")
                                                                val intent = Intent(
                                                                    context,
                                                                    AppMasterActivity::class.java
                                                                )
                                                                startActivity(intent)

                                                            } else {
                                                                Log.e(
                                                                    "Tag",
                                                                    "Response body is null"
                                                                )
                                                            }
                                                        } else {
                                                            try {
                                                                val errorBody =
                                                                    response.errorBody()?.string()
                                                                val errorJson = Gson().fromJson(
                                                                    errorBody,
                                                                    ErrorResponse::class.java
                                                                )
                                                                Log.e(
                                                                    "Tag",
                                                                    "Unsuccessful response: ${response.code()}, Title: ${errorJson.title}, Detail: ${errorJson.detail}"
                                                                )
                                                            } catch (e: Exception) {
                                                                Log.e(
                                                                    "Tag",
                                                                    "Failed to parse error response: ${e.message}",
                                                                    e
                                                                )
                                                            }
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<LoginResponse>,
                                                        t: Throwable
                                                    ) {
                                                        Log.e(
                                                            "Tag",
                                                            "Failed to make request: ${t.message}",
                                                            t
                                                        )
                                                    }

                                                })
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.e("Tag", "Response body is null")
                        }
                    }

                    override fun onFailure(call: Call<List<CardItem>>, t: Throwable) {
                        Log.e("Tag", "Response body is dsafadfafdasf")
                    }
                })
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileSelectorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileSelectorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}