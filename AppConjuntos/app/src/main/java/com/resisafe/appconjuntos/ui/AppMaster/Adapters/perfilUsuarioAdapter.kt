package com.resisafe.appconjuntos.ui.AppMaster.Adapters

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appconjuntos.ApiResponse
import com.resisafe.appconjuntos.ApiService
import com.resisafe.appconjuntos.ManejadorDeTokens
import com.resisafe.appconjuntos.R
import com.resisafe.appconjuntos.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


data class PerfilUsuario(
    val id: Int,
    val cedula: Int,
    val nombre: String,
    val tipoPerfil: String,
    val Torre: String,
    val Apto: Int,
    var activo: Int
)

class perfilUsuarioAdapter(var perfiles: MutableList<PerfilUsuario>, var listaOriginal : MutableList<PerfilUsuario>) :
    RecyclerView.Adapter<perfilUsuarioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): perfilUsuarioViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return perfilUsuarioViewHolder(
            layoutInflater.inflate(
                R.layout.item_conjunto_lista_perfiles,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: perfilUsuarioViewHolder, position: Int) {
        val item = perfiles[position]
        holder.render(item)


        val apiService = RetrofitClient.apiService

        holder.switchActivo.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (holder.token != null) {
                val estado = if (isChecked) 1 else 0
                val body = ApiService.CambiarEstadoRequestBody(estado)
                apiService.cambiarEstadoPerfil(body, perfiles[position].id.toInt(), holder.token)
                    .enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("AAAAAAAAAAAA",estado.toString())
                                perfiles[position].activo = estado;
                            } else {
                                Log.e("Tag", "Response body is null")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            Log.e("Tag", "Response body is dsafadfafdasf")
                        }
                    })
            }
        })


        holder.botonBorrar.setOnClickListener {
            if (holder.token != null) {
                apiService.eliminarPerfil(perfiles[position].id, holder.token)
                    .enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>
                        ) {
                            if (response.isSuccessful) {
                                val builder = AlertDialog.Builder(holder.context)
                                builder.setMessage("Se Borró el Perfil")
                                builder.setPositiveButton("Aceptar") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                val dialog = builder.create()
                                dialog.show()
                                perfiles.removeAt(position)
                                notifyDataSetChanged()
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
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    fun actualizar(perfiles: MutableList<PerfilUsuario>) {
        this.perfiles = perfiles
        notifyDataSetChanged()
    }

    fun getData(): MutableList<PerfilUsuario> {
        return listaOriginal

    }

    fun generarListaOriginal(toMutableList: MutableList<PerfilUsuario>) {
        listaOriginal = toMutableList;
        perfiles = toMutableList
        notifyDataSetChanged();
    }
}

class perfilUsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textoId = view.findViewById<TextView>(R.id.textoId)
    val textoCedulaUsuario = view.findViewById<TextView>(R.id.textoCedulaUsuario)

    val textoNombreUsuario = view.findViewById<TextView>(R.id.textoNombreUsuario)
    val textoTipoPerfil = view.findViewById<TextView>(R.id.textoTipoPerfil)
    val textoTorre = view.findViewById<TextView>(R.id.textoTorre)
    val textoApto = view.findViewById<TextView>(R.id.textoApto)
    val switchActivo = view.findViewById<Switch>(R.id.switchActivo)
    val botonBorrar = view.findViewById<ImageView>(R.id.botonBorrar)
    val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token;
    val context = view.context
    val icono = view.findViewById<ImageView>(R.id.imageView8)


    fun render(PerfilUsuario: PerfilUsuario) {

        textoId.text = PerfilUsuario.id.toString()
        textoCedulaUsuario.text = PerfilUsuario.cedula.toString()
        textoNombreUsuario.text = PerfilUsuario.nombre
        textoTipoPerfil.text = PerfilUsuario.tipoPerfil
        textoTorre.text = PerfilUsuario.Torre
        textoApto.text = PerfilUsuario.Apto.toString()
        switchActivo.isChecked = PerfilUsuario.activo == 1;

        when (PerfilUsuario.tipoPerfil) {
            "Administrador" -> {
                icono.setImageResource(R.drawable.manage_accounts)
            }

            "Residente" -> {
                icono.setImageResource(R.drawable.ic_baseline_person_24)
            }
            "Vigilante" -> {
                icono.setImageResource(R.drawable.shield_person)
            }
            "AppMaster" -> {
                icono.setImageResource(R.drawable.egineering)
            }
        }
    }
}
