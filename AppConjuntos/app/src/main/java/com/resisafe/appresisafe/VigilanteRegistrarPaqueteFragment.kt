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
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
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
 * Use the [VigilanteRegistrarPaqueteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VigilanteRegistrarPaqueteFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_vigilante_registrar_paquete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.actionBar?.title = "Registrar Paquete"


        val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token!!
        val apiService = RetrofitClient.apiService
        val args = arguments
        val idConjuntoArg = args?.getInt("idConjunto")!!
        val idPrefilActual = args?.getInt("idPrefilActual")!!


        val torreField = view.findViewById<TextInputEditText>(R.id.torreField)
        val aptoField = view.findViewById<TextInputEditText>(R.id.aptoField)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val buttonSeleccionarFecha = view.findViewById<Button>(R.id.buttonSeleccionarFecha)
        val buttonSeleccionarHora = view.findViewById<Button>(R.id.buttonSeleccionarHora)
        val editTextFecha =  view.findViewById<TextView>(R.id.editTextFecha)
        val editTextHora = view.findViewById<TextView>(R.id.editTextHora)

        buttonSeleccionarFecha.setOnClickListener(){
            mostrarDatePickerDialog(view    ,editTextFecha)
        }
        buttonSeleccionarHora.setOnClickListener(){
            mostrarTimePickerDialog(view,editTextHora)
        }
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener(){view.findNavController().popBackStack()}


        registerButton.setOnClickListener(){

            if(editTextFecha.text!!.isEmpty() || editTextFecha.text!!.isEmpty() || torreField.text!!.isEmpty() || aptoField.text!!.isEmpty()){
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Debe rellenar todos los campos")
                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

            else {
                val paquete = Paquete(
                    idPaquete = 0,
                    torre = torreField.text.toString().uppercase(),
                    apto = aptoField.text.toString().uppercase(),
                    idVigilanteRecibe = idPrefilActual,
                    estado = 1,
                    idResidenteRecibe = idPrefilActual,
                    fechaEntrega = editTextFecha.text.toString(),
                    horaEntrega = editTextHora.text.toString(),
                    fechaRecibido = "2024-01-01",
                    horaRecibido = "00:00:00",
                    idConjunto = idConjuntoArg
                )
                registrarPaquete(paquete,token,apiService,view)
            }
        }


        super.onViewCreated(view, savedInstanceState)
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

    fun registrarPaquete(paquete: Paquete,token:String,apiService: ApiService,view: View){
        apiService.registrarPaquete(paquete, token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    if (datos != null) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Paquete registrado con éxito")
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VigilanteRegistrarPaqueteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VigilanteRegistrarPaqueteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}