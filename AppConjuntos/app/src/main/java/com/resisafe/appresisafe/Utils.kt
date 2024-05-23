package com.resisafe.appresisafe

import android.app.AlertDialog
import android.view.View

class Utils{



    companion object {
        fun mostrarDialogoInformacion(mensaje:String,view: View){
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage(mensaje)
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

}