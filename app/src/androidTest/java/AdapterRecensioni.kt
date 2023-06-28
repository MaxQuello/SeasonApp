import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.Recensioni
import com.example.seasonapp.databinding.CardViewRecensioniBinding

class AdapterRecensioni(private val data: ArrayList<Recensioni>): RecyclerView.Adapter<AdapterRecensioni.ViewHolder>() {
    class ViewHolder(val binding: CardViewRecensioniBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recensioni){
            binding.recensione.text = item.recensione
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewRecensioniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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