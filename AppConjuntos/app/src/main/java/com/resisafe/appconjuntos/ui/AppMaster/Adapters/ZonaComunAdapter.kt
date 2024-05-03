package com.resisafe.appconjuntos.ui.AppMaster.Adapters

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appconjuntos.ApiResponse
import com.resisafe.appconjuntos.ManejadorDeTokens
import com.resisafe.appconjuntos.R
import com.resisafe.appconjuntos.RetrofitClient
import com.resisafe.appconjuntos.UserData
import com.resisafe.appconjuntos.ZonaComun
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ZonaComunAdapter(var zonasComunes: MutableList<ZonaComun>) :
    RecyclerView.Adapter<ZonaComunViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZonaComunViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ZonaComunViewHolder(
            layoutInflater.inflate(
                R.layout.item_zonacomun_lista,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ZonaComunViewHolder, position: Int) {
        val item = zonasComunes[position]

        holder.botonZonaComunInfo.setOnClickListener(){
            val bundle = Bundle()
            bundle.putInt("idZonaComun", holder.idZonaComun)
            val navController = Navigation.findNavController(holder.itemView)
            navController!!.navigate(R.id.action_fragmentConjuntoListaZonascomunes_to_zonacomunInfoFragment,bundle)
        }
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return zonasComunes.size
    }

    fun actualizar(zonasComunes: MutableList<ZonaComun>) {
        this.zonasComunes = zonasComunes
        notifyDataSetChanged()
    }

    fun getData(): MutableList<ZonaComun> {
        return zonasComunes
    }
}

class ZonaComunViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView = view.findViewById(R.id.imageView8)
    val nombreZonaComun: TextView = view.findViewById(R.id.nombreZonaComun)
    val aforoZonaComun: TextView = view.findViewById(R.id.aforoZonaComun)
    val horarioApertura: TextView = view.findViewById(R.id.horarioApertura)
    val horarioCierre: TextView = view.findViewById(R.id.horarioCierre)
    val botonZonaComunInfo: ImageView = view.findViewById(R.id.botonZonaComunInfo)

    val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token;
    val context = view.context
    var idZonaComun : Int = 0

    fun render(ZonaComun: ZonaComun) {

        nombreZonaComun.text = ZonaComun.nombre
        aforoZonaComun.text = "Aforo: " + ZonaComun.aforoMaximo
        horarioApertura.text = ZonaComun.horarioApertura
        horarioCierre.text = ZonaComun.horarioCierre
        idZonaComun = ZonaComun.idZonaComun




    }
}
