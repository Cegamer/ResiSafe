package com.resisafe.appresisafe

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import android.widget.TimePicker
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VigilanteRegistroVisitaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VigilanteRegistroVisitaFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_vigilante_registro_visita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buscadorCedulaVisitante = view.findViewById<TextInputEditText>(R.id.cedulaField)
        val botonNuevoVisitante = view.findViewById<ImageButton>(R.id.agregarVisitante)
        val visitanteNombreApellidoText = view.findViewById<TextView>(R.id.visitanteNombreApellidoText)
        val idVisitanteText = view.findViewById<TextView>(R.id.idVisitante)

        val buscadorCedulaResidente = view.findViewById<TextInputEditText>(R.id.cedulaField1)
        val residenteNombreApellidoText = view.findViewById<TextView>(R.id.nombreApellido)
        val idResidenteText = view.findViewById<TextView>(R.id.idResidente)

        val editTextFecha = view.findViewById<TextView>(R.id.editTextFecha)
        val botonSeleccionarFecha = view.findViewById<Button>(R.id.buttonSeleccionarFecha)

        val editTextHoraIngreso = view.findViewById<TextView>(R.id.editTextHoraIngreso)
        val buttonApertura = view.findViewById<Button>(R.id.buttonApertura)

        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)

        val tokenResponse = ManejadorDeTokens.cargarTokenUsuario(view.context)!!
        val apiService = RetrofitClient.apiService

        val args = arguments
        val idConjunto = args?.getInt("idConjunto")!!

        buscadorCedulaVisitante.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                buscarVisitantePorCedula(tokenResponse.token,apiService,buscadorCedulaVisitante,visitanteNombreApellidoText,idVisitanteText)
                return@OnEditorActionListener true
            }
            false
        })

        buscadorCedulaResidente.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                buscarResidentePorCedula(tokenResponse.token,apiService,buscadorCedulaResidente,residenteNombreApellidoText,idResidenteText,idConjunto)
                return@OnEditorActionListener true
            }
            false
        })

        botonSeleccionarFecha.setOnClickListener(){
            mostrarDatePickerDialog(view,editTextFecha)
        }

        buttonApertura.setOnClickListener(){
            mostrarTimePickerDialog(view,editTextHoraIngreso)
        }

        botonNuevoVisitante.setOnClickListener(){
            view.findNavController().navigate(
                R.id.action_vigilanteRegistroVisitaFragment_to_vigilanteRegistroVisitanteFragment
            )
        }

        cancelButton.setOnClickListener(){
            view.findNavController().popBackStack()
        }

        registerButton.setOnClickListener(){
            viewLifecycleOwner.lifecycleScope.launch() {

                val prefilVigilante: PerfilesDTO = ManejadorDeTokens.cargarPerfilActual(view.context)!!
                val visita = Visita(
                    idRegistro = 0,
                    idVisitante = idVisitanteText.text.toString().toInt(),
                    idResidenteVinculado = idResidenteText.text.toString().toInt(),
                    idVigilanteQueRegistra = prefilVigilante.idPerfil,
                    fecha = editTextFecha.text.toString(),
                    horaIngreso = editTextHoraIngreso.text.toString(),
                    horaSalida = "00:00:00"
                )
                registrarVisita(visita,tokenResponse.token, apiService, view)
            }
        }


        super.onViewCreated(view, savedInstanceState)
    }

    fun registrarVisita(visita: Visita,token:String,apiService: ApiService,view: View){
        apiService.registrarVisita(visita, token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    if (datos != null) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Visita registrada con éxito")
                        builder.setPositiveButton("Aceptar") { dialog, _ ->
                            findNavController().popBackStack()
                            dialog.dismiss()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                } else {
                    Log.e("Tag", "Response body is null")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Tag", "Response body is dsafadfafdasf")
            }
        })
    }


    private fun mostrarDatePickerDialog(view: View,editText: TextView) {
        val cal = Calendar.getInstance()
        val año = cal.get(Calendar.YEAR)
        val mes = cal.get(Calendar.MONTH)
        val día = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            view.context,
            DatePickerDialog.OnDateSetListener { view: DatePicker, añoSeleccionado: Int, mesSeleccionado: Int, díaSeleccionado: Int ->
                val fechaFormateada = String.format("%04d-%02d-%02d", añoSeleccionado, mesSeleccionado + 1, díaSeleccionado)
                editText.setText(fechaFormateada)
            },
            año,
            mes,
            día
        )
        datePickerDialog.show()
    }

    private fun mostrarTimePickerDialog(view: View,editText: TextView) {
        val cal = Calendar.getInstance()
        val hora = cal.get(Calendar.HOUR_OF_DAY)
        val minuto = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { view: TimePicker, horaSeleccionada: Int, minutoSeleccionado: Int ->
                val horaFormateada =
                    String.format("%02d:%02d:00", horaSeleccionada, minutoSeleccionado)
                editText.setText(horaFormateada)
            },
            hora,
            minuto,
            true
        )
        timePickerDialog.show()
    }

    fun buscarVisitantePorCedula(token:String,apiService: ApiService,buscadorCedula : TextInputEditText, textoResultado : TextView,textoId:TextView){
        RetrofitClient.apiService.getvisitanteByCedula(buscadorCedula.text.toString().toInt(), token).enqueue(object : Callback<visitante> {
            override fun onResponse(call: Call<visitante>, response: Response<visitante>) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    if (datos != null) {
                        textoResultado.text = datos.nombre + " " + datos.apellido
                        textoId.text = datos.idVisitante.toString()
                    }
                } else {
                    textoResultado.text = "no encontrado"
                }
            }
            override fun onFailure(call: Call<visitante>, t: Throwable) {
            }
        })
    }


    fun buscarResidentePorCedula(token:String,apiService: ApiService,buscadorCedula : TextInputEditText, textoResultado : TextView,textoId:TextView,idConjunto:Int){
        RetrofitClient.apiService.getPerfilByCedula(buscadorCedula.text.toString().toInt(), idConjunto,token).enqueue(object : Callback<perfilByCedula> {
            override fun onResponse(call: Call<perfilByCedula>, response: Response<perfilByCedula>) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    if (datos != null) {
                        textoResultado.text = datos.nombreApellido
                        textoId.text = datos.idPerfil.toString()
                    }
                } else {
                    textoResultado.text = "no encontrado"
                }
            }
            override fun onFailure(call: Call<perfilByCedula>, t: Throwable) {
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
         * @return A new instance of fragment VigilanteRegistroVisitaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VigilanteRegistroVisitaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}