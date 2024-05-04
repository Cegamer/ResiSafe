import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.resisafe.appresisafe.R

class CustomAdapter(private val nombres: List<String>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listavisitante, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nombreCompleto = nombres[position].split(" ")
        val nombre = nombreCompleto[0]
        val apellido = nombreCompleto.getOrElse(1) { "" } // Si no hay apellido, mostrar cadena vacía
        val cedula = nombreCompleto.getOrElse(2) { "" } // Si no hay cédula, mostrar cadena vacía
        holder.bind(nombre, apellido, cedula)
    }

    override fun getItemCount(): Int {
        return nombres.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardView)
        private val nombreTextView: TextView = itemView.findViewById(R.id.textNombre)
        private val apellidoTextView: TextView = itemView.findViewById(R.id.textApellido)
        private val cedulaTextView: TextView = itemView.findViewById(R.id.textCedula)

        fun bind(nombre: String, apellido: String, cedula: String) {
            nombreTextView.text = nombre
            apellidoTextView.text = apellido
            cedulaTextView.text = cedula

        }
    }
}
