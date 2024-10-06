import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.correct.alahmdy.R
import com.correct.alahmdy.data.user.Tasbeh
import com.correct.alahmdy.helper.ClickListener

class TasbehAdapter(
    private var list: List<Tasbeh>,
    private val listener: ClickListener
) : RecyclerView.Adapter<TasbehAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasbehAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tasbeh_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasbehAdapter.ViewHolder, position: Int) {
        holder.txt.text = list[position].tasbeh
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: List<Tasbeh>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt: TextView = itemView.findViewById(R.id.txt_tasbeh)

        init {
            itemView.setOnClickListener {
                listener.onItemClickListener(adapterPosition)
            }
        }
    }
}