import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.PrenotazioniEffettuate
import com.example.seasonapp.Recensioni
import com.example.seasonapp.databinding.CardViewPrenotazioniEffettuateBinding

class AdapterPrenotazioniEffettuate(private val data: ArrayList<PrenotazioniEffettuate>): RecyclerView.Adapter<AdapterPrenotazioniEffettuate.ViewHolder>() {
    class ViewHolder(val binding: CardViewPrenotazioniEffettuateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PrenotazioniEffettuate){

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewPrenotazioniEffettuateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }
}