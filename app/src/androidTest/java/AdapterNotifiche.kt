import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.Notifica
import com.example.seasonapp.databinding.CardViewNotificheBinding

class AdapterNotifiche(private val data: ArrayList<Notifica>): RecyclerView.Adapter<AdapterNotifiche.ViewHolder>() {
    class ViewHolder(val binding: CardViewNotificheBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Notifica){
            binding.notifica.text = item.notifica
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewNotificheBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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