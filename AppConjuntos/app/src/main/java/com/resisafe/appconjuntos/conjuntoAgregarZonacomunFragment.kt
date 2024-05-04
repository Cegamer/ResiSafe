package com.resisafe.appconjuntos

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [conjuntoAgregarZonacomunFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class conjuntoAgregarZonacomunFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_conjunto_agregar_zonacomun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val editTextNombre = view.findViewById<TextInputEditText>(R.id.editTextNombre)
        val editTextAforoMaximo = view.findViewById<TextInputEditText>(R.id.editTextAforoMaximo)
        val editTextPrecio = view.findViewById<TextInputEditText>(R.id.editTextPrecio)
        val editTextIntervaloTurnos = view.findViewById<TextInputEditText>(R.id.editTextIntervaloTurnos)

        val editTextApertura = view.findViewById<TextView>(R.id.editTextApertura)
        val editTextCierre = view.findViewById<TextView>(R.id.editTextCierre)


        val buttonApertura = view.findViewById<Button>(R.id.buttonApertura)
        buttonApertura.setOnClickListener {
            mostrarTimePickerDialog(editTextApertura)
        }

        // Configurar clic en el botón de selección para el cierre
        val buttonCierre = view.findViewById<Button>(R.id.buttonCierre)
        buttonCierre.setOnClickListener {
            mostrarTimePickerDialog(editTextCierre)
        }


        fun obtenerDatos(): ZonaComun {

            var idConjuntoArg = 0
            val args = arguments
            if (args != null) {
                idConjuntoArg = args.getInt("idConjunto")
            }

            val nombre = editTextNombre.text.toString()
            val apertura = editTextApertura.text.toString()
            val cierre = editTextCierre.text.toString()
            val aforoMaximo = editTextAforoMaximo.text.toString().toIntOrNull() ?: 0
            val precio = editTextPrecio.text.toString().toIntOrNull() ?: 0
            val intervaloTurnos = editTextIntervaloTurnos.text.toString().toIntOrNull() ?: 0

            if (nombre.isEmpty() || apertura.isEmpty() || cierre.isEmpty() || aforoMaximo == 0 || intervaloTurnos == 0) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Ingrese todos los datos")
                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

            val zonaComun = ZonaComun(
                idZonaComun = 0,
                idConjunto = idConjuntoArg,
                nombre = nombre,
                horarioApertura = apertura,
                horarioCierre = cierre,
                aforoMaximo = aforoMaximo,
                precio = precio,
                idIcono = 1,
                intervaloTurnos = intervaloTurnos
            )

            return zonaComun

        }


        val buttonGuardar = view.findViewById<Button>(R.id.buttonGuardar)
        buttonGuardar.setOnClickListener {
            val apiService = RetrofitClient.apiService
            val context = this.requireContext()
            val token = ManejadorDeTokens.cargarTokenUsuario(context)?.token;

            if (token != null) {
                val zonaComun = obtenerDatos()
                apiService.crearZonaComun(zonaComun, token).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {
                        if (response.isSuccessful) {
                            val datos = response.body()
                            if (datos != null) {
                                val builder = AlertDialog.Builder(context)
                                builder.setMessage("Creación Exitosa")
                                builder.setPositiveButton("Aceptar") { dialog, _ ->
                                    findNavController().popBackStack()
                                    dialog.dismiss() // Cierra el diálogo
                                }

                                // Crear y mostrar el diálogo
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
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun mostrarTimePickerDialog(editText: TextView) {
        val cal = Calendar.getInstance()
        val hora = cal.get(Calendar.HOUR_OF_DAY)
        val minuto = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { view: TimePicker, horaSeleccionada: Int, minutoSeleccionado: Int ->
                val horaFormateada =
                    String.format("%02d:%02d", horaSeleccionada, minutoSeleccionado)
                editText.setText(horaFormateada)
            },
            hora,
            minuto,
            true
        )
        timePickerDialog.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment conjuntoAgregarZonacomunFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            conjuntoAgregarZonacomunFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}