import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.correct.alahmdy.R
import com.correct.alahmdy.helper.ClickListener

class SurahAdapter(
    private var list: List<Int>,
    private val listener: ClickListener
) : RecyclerView.Adapter<SurahAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.surah_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurahAdapter.ViewHolder, position: Int) {
        holder.img.setImageResource(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: List<Int>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.main)
        init {
            img.setOnClickListener{
                listener.onItemClickListener(adapterPosition)
            }
            itemView.setOnClickListener {
                listener.onItemClickListener(adapterPosition)
            }
        }
    }
}