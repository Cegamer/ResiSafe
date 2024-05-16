package com.resisafe.appresisafe.ui.AppMaster.Adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appresisafe.ManejadorDeTokens
import com.resisafe.appresisafe.R
import com.resisafe.appresisafe.VisitaData
import com.resisafe.appresisafe.ZonaComun

class VisitasAdapter(var visitas: MutableList<VisitaData>,var listaOriginal : MutableList<VisitaData>) :
    RecyclerView.Adapter<VisitasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VisitasViewHolder(
            layoutInflater.inflate(
                R.layout.item_listavisitante,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VisitasViewHolder, position: Int) {
        val item = visitas[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return visitas.size
    }

    fun actualizar(visitas: MutableList<VisitaData>) {
        this.visitas = visitas
        notifyDataSetChanged()
    }

    fun getData(): MutableList<VisitaData> {
        return listaOriginal
    }

    fun generarListaOriginal(toMutableList: MutableList<VisitaData>) {
        listaOriginal = toMutableList;
        visitas = toMutableList
        notifyDataSetChanged();
    }
}

class VisitasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cedulaText: TextView = view.findViewById(R.id.cedulaText)
    val nombreVisitante: TextView = view.findViewById(R.id.nombreVisitante)
    val fechaVisita: TextView = view.findViewById(R.id.fechaVisita)
    val horaIngreso: TextView = view.findViewById(R.id.horaIngreso)
    val nombreResidenteVinculado: TextView = view.findViewById(R.id.nombreResidenteVinculado)
    val cedulaResidenteVinculado: TextView = view.findViewById(R.id.cedulaResidenteVinculado)

    val token = ManejadorDeTokens.cargarTokenUsuario(view.context)?.token;
    val context = view.context

    fun render(Visita: VisitaData) {
        cedulaText.text = Visita.cedula.toString()
        nombreVisitante.text = Visita.nombre + " " +Visita.apellido
        fechaVisita.text = Visita.fecha
        horaIngreso.text = Visita.horaIngreso
        nombreResidenteVinculado.text = Visita.nombreResidente + " " +Visita.apellidoResidente
        cedulaResidenteVinculado.text = Visita.cedulaResidente.toString()
    }
}