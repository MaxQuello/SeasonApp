import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.Offerta
import com.example.seasonapp.databinding.CardViewOfferteBinding

class AdapterOfferte(private val data: ArrayList<Offerta>): RecyclerView.Adapter<AdapterOfferte.ViewHolder>() {
    class ViewHolder(val binding: CardViewOfferteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Offerta){
            binding.nSingola.text = item.nSingola.toString()
            binding.nDoppia.text = item.nDoppia.toString()
            binding.nFamiliare.text = item.nFamiliare.toString()
            binding.prezzoOfferta.text = item.prezzo.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewOfferteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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