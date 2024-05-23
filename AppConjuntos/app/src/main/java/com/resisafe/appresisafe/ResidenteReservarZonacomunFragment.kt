package com.resisafe.appresisafe

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
 * Use the [ResidenteReservarZonacomunFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResidenteReservarZonacomunFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_residente_reservar_zonacomun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.actionBar?.title = "Reservar Zona Común"


        val textoFecha = view.findViewById<TextView>(R.id.textoFecha)
        val botonSeleccionarFecha = view.findViewById<Button>(R.id.botonSeleccionarFecha)
        val textoHora = view.findViewById<TextView>(R.id.textoHora)
        val botonSeleccionarHora = view.findViewById<Button>(R.id.botonSeleccionarHora)
        val textoCuposDisponibles = view.findViewById<TextView>(R.id.textoCuposDisponibles)
        val editTextCupos = view.findViewById<EditText>(R.id.editTextCupos)
        val buttonReservar = view.findViewById<Button>(R.id.buttonReservar)

        val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token!!
        val apiService = RetrofitClient.apiService
        val args = arguments
        val idConjuntoArg = args?.getInt("idConjunto")!!
        val tipoSolicitanteArg = args?.getInt("tipoSolicitante")!!
        val idPrefilActual = args?.getInt("idPrefilActual")!!


        val zonasComunesMap: MutableMap<String, Int> = mutableMapOf()


        val spinnerZonaComun = view.findViewById<Spinner>(R.id.spinnerZonaComun)
        viewLifecycleOwner.lifecycleScope.launch() {
            val ZonasComunes = cargarZonasComunesConjunto(idConjuntoArg,token,apiService)!!
            for(zonacomun in ZonasComunes){
                zonasComunesMap[zonacomun.nombre] = zonacomun.idZonaComun
            }
            val arrayZonasComunes: List<String> = zonasComunesMap.keys.toList();
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_box,
                arrayZonasComunes
            )
            adapter.setDropDownViewResource(R.layout.spinner_item)
            spinnerZonaComun.adapter = adapter
        }

        var horarioSeleccionado: HorarioDisponible? = null;
        fun updateHorario (){
            if(horarioSeleccionado == null){
                textoHora.text =""
                textoCuposDisponibles.text = "0"
            }
            else{
                textoHora.text = horarioSeleccionado!!.horaInicio + " - " +horarioSeleccionado!!.horaFin
                textoCuposDisponibles.text = horarioSeleccionado!!.cuposDisponibles.toString()
            }
        }
        botonSeleccionarFecha.setOnClickListener(){
            mostrarDatePickerDialog(view,textoFecha)
            horarioSeleccionado = null
            updateHorario()
        }



        botonSeleccionarHora.setOnClickListener(){
            if(textoFecha.text != ""){
                viewLifecycleOwner.lifecycleScope.launch() {
                    val idZonaComun = zonasComunesMap[spinnerZonaComun.selectedItem.toString()]!!
                    val horariosDisponibles : List<HorarioDisponible> = cargarHorariosZonaComun(idZonaComun,textoFecha.text.toString(),token,apiService)!!
                    val builderAlert = AlertDialog.Builder(context)

                    val mapHorariosDisponibles: MutableMap<String,HorarioDisponible> = mutableMapOf()
                    for(horario in horariosDisponibles){
                        val key = horario.horaInicio + " - " + horario.horaFin + "\nCupos Disponibles: " + horario.cuposDisponibles
                        mapHorariosDisponibles[key]  = horario
                    }
                    val opciones : Array<String> = mapHorariosDisponibles.keys.toTypedArray()


                    var defaultPosition = 0
                    builderAlert.setTitle("Seleccionar Horario de Reserva")
                    builderAlert.setPositiveButton("Seleccionar"){
                        dialog, _ ->
                        horarioSeleccionado = mapHorariosDisponibles[opciones[defaultPosition]]
                        updateHorario()
                        dialog.dismiss()
                    }
                    builderAlert.setSingleChoiceItems(opciones,defaultPosition){ dialog, which ->
                        defaultPosition = which
                    }
                    builderAlert.show()
                }

            }
            else {
                mostrarDialogo("Debe seleccionar la fecha para ver los horarios disponibles",view)
            }

        }


        buttonReservar.setOnClickListener(){
            if(horarioSeleccionado != null && editTextCupos.text.toString().isNotEmpty()){
                if(editTextCupos.text.toString().toInt() > horarioSeleccionado!!.cuposDisponibles)
                    mostrarDialogo("No hay suficientes cupos para la cantidad de personas seleccionada",view)
                else{
                    val datos = Reserva(
                        idReserva = 0,
                        idReservante = idPrefilActual,
                        idZonaComun = zonasComunesMap[spinnerZonaComun.selectedItem.toString()]!!,
                        fecha = textoFecha.text.toString(),
                        horaInicio = horarioSeleccionado!!.horaInicio,
                        horaFin = horarioSeleccionado!!.horaFin,
                        cantidadPersonas = editTextCupos.text.toString().toInt(),
                        estado = 1
                    )

                    registrarReserva(datos,token,apiService,view)
                }
            }
            else   {
                mostrarDialogo("Debe llenar todos los campos",view)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private  fun mostrarDialogo(mensaje: String,view: View){
        val builder = AlertDialog.Builder(view.context)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun registrarReserva(reserva: Reserva,token:String,apiService: ApiService,view: View){
        apiService.registrarReserva(reserva, token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    if (datos != null) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Reserva registrada con éxito")
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

    private suspend fun cargarHorariosZonaComun(idZonaComun:Int,fecha:String,token:String,apiService: ApiService): List<HorarioDisponible>? {
        val horariosDisponibles = apiService.getHorariosDisponiblesZonaComun(idZonaComun,fecha, token).awaitResponse().body()
        return  horariosDisponibles
    }
    private suspend fun cargarZonasComunesConjunto(idConjunto:Int,token:String,apiService: ApiService): List<ZonaComun>? {

        val listaVisitas = apiService.getZonasComunesConjunto(idConjunto, token).awaitResponse().body()
        return  listaVisitas
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResidenteReservarZonacomunFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResidenteReservarZonacomunFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}