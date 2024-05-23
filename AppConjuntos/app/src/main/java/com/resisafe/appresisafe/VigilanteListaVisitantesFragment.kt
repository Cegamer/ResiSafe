package com.resisafe.appresisafe

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appresisafe.ui.AppMaster.Adapters.VisitasAdapter
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VigilanteListaVisitantesFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_vigilante_lista_visitantes, container, false)
        activity?.actionBar?.title = "Lista de Visitantes"


        val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token!!
        val apiService = RetrofitClient.apiService
        val args = arguments
        val idConjuntoArg = args?.getInt("idConjunto")!!
        val tipoSolicitanteArg = args?.getInt("tipoSolicitante")!!
        val idPrefilActual = args?.getInt("idPrefilActual")!!



        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerUsuarios)
        val filtroCedulaUsuario = view.findViewById<EditText>(R.id.filtroCedulaUsuario)
        val editTextFecha = view.findViewById<TextView>(R.id.editTextFecha)
        val buttonSeleccionarFecha = view.findViewById<Button>(R.id.buttonSeleccionarFecha)
        val buttonLimpiar = view.findViewById<Button>(R.id.buttonLimpiar)

        buttonSeleccionarFecha.setOnClickListener(){mostrarDatePickerDialog(view,editTextFecha)}
        buttonLimpiar.setOnClickListener(){ editTextFecha.text ="";}
               viewLifecycleOwner.lifecycleScope.launch() {
            var visitaData :List<VisitaData> = listOf()
            if(tipoSolicitanteArg == 3 || tipoSolicitanteArg == 1)
                 visitaData = cargarVisitasConjunto(idConjuntoArg, token, apiService)!!
            if(tipoSolicitanteArg == 2)
                 visitaData = cargarVisitasUsuario(idPrefilActual, token, apiService)!!

            val adapter = VisitasAdapter(visitaData.toMutableList(),visitaData.toMutableList())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)


            fun aplicarFiltrado(){
                val datosFiltrados = adapter.getData().filter { VisitaData ->
                    VisitaData.cedula.toString().contains(filtroCedulaUsuario.text) }
                    .filter { VisitaData -> VisitaData.fecha.contains(editTextFecha.text)}
                adapter.actualizar(datosFiltrados.toMutableList())
            }

            editTextFecha.addTextChangedListener { aplicarFiltrado() }
            filtroCedulaUsuario.addTextChangedListener { aplicarFiltrado() }
        }


        return view
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


    private suspend fun cargarVisitasConjunto(idConjunto:Int,token:String,apiService: ApiService): List<VisitaData>? {
        val listaVisitas = apiService.getVisitantesByConjunto(idConjunto, token).awaitResponse().body()
        return  listaVisitas
    }
    private suspend fun cargarVisitasUsuario(idResidente:Int,token:String,apiService: ApiService): List<VisitaData>? {
        val listaVisitas = apiService.getVisitantesByResidente(idResidente, token).awaitResponse().body()
        return  listaVisitas
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VigilanteListaVisitantesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
