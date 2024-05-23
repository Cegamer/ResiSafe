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
import android.widget.TextView
import android.widget.TimePicker
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditarEstadoDePaqueteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarEstadoDePaqueteFragment : Fragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token!!
        val apiService = RetrofitClient.apiService
        val args = arguments
        val idPaquete = args?.getInt("idPaquete")!!
        //Cargar info paquete

        val cedulaBuscador = view.findViewById<TextInputEditText>(R.id.cedulaBuscador)
        val editTextFecha = view.findViewById<TextView>(R.id.editTextFecha)
        val editTextHora = view.findViewById<TextView>(R.id.editTextHora)
        val buttonSeleccionarFecha = view.findViewById<Button>(R.id.buttonSeleccionarFecha)
        val buttonSeleccionarHora = view.findViewById<Button>(R.id.buttonSeleccionarHora)
        val codigoPaquete = view.findViewById<TextView>(R.id.codigoPaquete)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        val idUsuarioText = view.findViewById<TextView>(R.id.idUsuarioText)

        fun renderizarDatos(paquete: Paquete) {
            codigoPaquete.text = "Paquete: " + paquete.idPaquete
        }


        var paquete: Paquete? = null;
        viewLifecycleOwner.lifecycleScope.launch() {
            paquete = cargarInformacionPaquete(idPaquete, token, apiService)!!
            renderizarDatos(paquete!!)
        }

        var usuarioCedula: perfilByCedula? = null

        cedulaBuscador.setOnEditorActionListener(TextView.OnEditorActionListener() { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewLifecycleOwner.lifecycleScope.launch() {
                    Log.d("Paquete",paquete.toString())
                    usuarioCedula = cargarUsuarioPorCedula(cedulaBuscador.text.toString().toInt(), paquete!!.idConjunto, token, apiService)!!
                    idUsuarioText.text = usuarioCedula!!.nombreApellido
                }
                return@OnEditorActionListener true
            }
            false
        })
        buttonSeleccionarFecha.setOnClickListener(){mostrarDatePickerDialog(view,editTextFecha)}
        buttonSeleccionarHora.setOnClickListener(){mostrarTimePickerDialog(view,editTextHora)}

        fun validarcampos():Boolean{
            if(usuarioCedula == null) return false
            if(paquete == null) return  false
            if(editTextFecha.text.toString().isEmpty()) return  false
            if(editTextHora.text.toString().isEmpty()) return  false
            else return  true
        }

        registerButton.setOnClickListener(){
            val paqueteEditado = Paquete(
                 idPaquete = paquete!!.idPaquete,
             torre = paquete!!.torre,
             apto = paquete!!.apto,
             idVigilanteRecibe = paquete!!.idVigilanteRecibe,
             estado = 2,
             idResidenteRecibe = usuarioCedula!!.idPerfil,
             fechaEntrega = paquete!!.fechaEntrega,
             horaEntrega = paquete!!.horaEntrega,
             fechaRecibido = editTextFecha.text.toString(),
             horaRecibido = editTextHora.text.toString(),
             idConjunto = paquete!!.idConjunto
            )
            if(validarcampos()){
            registrarPaquete(paqueteEditado,token,apiService,view)}
            else{
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Debe rellenar todos los campos correctamente")
                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    findNavController().popBackStack()
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun registrarPaquete(paquete: Paquete,token:String,apiService: ApiService,view: View){
        apiService.editarEstadoPaquete(paquete.idPaquete,paquete, token).enqueue(object : Callback<ApiResponse> {
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
    suspend fun cargarUsuarioPorCedula(cedula: Int, idConjunto: Int, token: String, apiService: ApiService): perfilByCedula {
        return apiService.getPerfilByCedula(cedula, idConjunto, token).awaitResponse().body()!!
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

    private suspend fun cargarInformacionPaquete(
        idPaquete: Int,
        token: String,
        apiService: ApiService
    ): Paquete {
        val paquete =
            apiService.obtenerPaqueteById(idPaquete, token).awaitResponse().body()!!
        return paquete
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_estado_de_paquete, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditarEstadoDePaqueteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarEstadoDePaqueteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}