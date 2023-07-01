import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.Gym
import com.example.seasonapp.Recensioni
import com.example.seasonapp.databinding.CardViewGympassBinding


class AdapterGym(private val data: ArrayList<Gym>): RecyclerView.Adapter<AdapterGym.ViewHolder>() {
    class ViewHolder(val binding: CardViewGympassBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Gym){

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewGympassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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