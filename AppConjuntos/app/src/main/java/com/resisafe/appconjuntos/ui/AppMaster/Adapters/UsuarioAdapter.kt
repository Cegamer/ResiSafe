package com.resisafe.appconjuntos.ui.AppMaster.Adapters

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appconjuntos.ApiResponse
import com.resisafe.appconjuntos.ManejadorDeTokens
import com.resisafe.appconjuntos.R
import com.resisafe.appconjuntos.RetrofitClient
import com.resisafe.appconjuntos.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsuarioAdapter(var usuarios: MutableList<UserData>) :
    RecyclerView.Adapter<UsuarioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuarioViewHolder(
            layoutInflater.inflate(
                R.layout.item_usuario_lista,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val item = usuarios[position]
        holder.render(item)
        val apiService = RetrofitClient.apiService
        holder.botonBorrar.setOnClickListener {
            if (holder.token != null) {
                apiService.deleteUser(holder.idUsuario, holder.token).enqueue(object :
                    Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {
                        if (response.isSuccessful) {
                            val builder = AlertDialog.Builder(holder.context)
                            builder.setMessage("Se Borró el Usuario")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }
                            val dialog = builder.create()
                            dialog.show()
                            usuarios.removeAt(position)
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
        return usuarios.size
    }

    fun actualizar(usuarios: MutableList<UserData>) {
        this.usuarios = usuarios
        notifyDataSetChanged()
    }

    fun getData(): MutableList<UserData> {
        return usuarios
    }
}

class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textoId = view.findViewById<TextView>(R.id.textoId)
    val textoCedulaUsuario = view.findViewById<TextView>(R.id.textoCedulaUsuario)
    val textoNombreUsuario = view.findViewById<TextView>(R.id.textoNombreUsuario)
    val textoTipoPerfil = view.findViewById<TextView>(R.id.textoApellidoUsuario)
    val botonBorrar = view.findViewById<ImageView>(R.id.botonBorrar)
    val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token;
    val context = view.context
    var idUsuario : Int = 0

    fun render(Usuario: UserData) {
        textoId.text = Usuario.idUsuario.toString()
        textoCedulaUsuario.text = Usuario.cedula.toString()
        textoNombreUsuario.text = Usuario.nombre
        textoTipoPerfil.text = Usuario.apellido
        idUsuario = Usuario.idUsuario
    }
}
