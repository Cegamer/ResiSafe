package com.resisafe.appconjuntos.ui.AppMaster.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appconjuntos.R

data class PerfilUsuario(val id: Int, val cedula:Int, val nombre:String, val tipoPerfil:String,val Torre: String, val Apto: Int, val activo:Int)

class perfilUsuarioAdapter(var perfiles:List<PerfilUsuario> ) : RecyclerView.Adapter<perfilUsuarioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): perfilUsuarioViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return  perfilUsuarioViewHolder(layoutInflater.inflate(R.layout.conjunto_lista_perfiles_item,parent,false))
    }

    override fun onBindViewHolder(holder: perfilUsuarioViewHolder, position: Int) {
        val item = perfiles[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    fun actualizar(perfiles:List<PerfilUsuario> ){
        this.perfiles = perfiles
        notifyDataSetChanged()
    }


}

class perfilUsuarioViewHolder (view: View) : RecyclerView.ViewHolder(view){
    val textoId = view.findViewById<TextView>(R.id.textoId)
    val textoCedulaUsuario = view.findViewById<TextView>(R.id.textoCedulaUsuario)

    val textoNombreUsuario = view.findViewById<TextView>(R.id.textoNombreUsuario)
    val textoTipoPerfil = view.findViewById<TextView>(R.id.textoTipoPerfil)
    val textoTorre = view.findViewById<TextView>(R.id.textoTorre)
    val textoApto = view.findViewById<TextView>(R.id.textoApto)
    val switchActivo = view.findViewById<Switch>(R.id.switchActivo)
    val botonBorrar = view.findViewById<ImageView>(R.id.botonBorrar)


    fun render(PerfilUsuario: PerfilUsuario){

        textoId.text = PerfilUsuario.id.toString()
        textoCedulaUsuario.text = PerfilUsuario.cedula.toString()
        textoNombreUsuario.text = PerfilUsuario.nombre
        textoTipoPerfil.text = PerfilUsuario.tipoPerfil
        textoTorre.text = PerfilUsuario.Torre
        textoApto.text = PerfilUsuario.Apto.toString()
        switchActivo.isChecked = PerfilUsuario.activo == 1;
        botonBorrar.setOnClickListener{
            //Implementar borrado de Perfiles
        }
    }

}
