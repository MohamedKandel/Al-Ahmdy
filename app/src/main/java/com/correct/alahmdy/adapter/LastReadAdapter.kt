import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.correct.alahmdy.R
import com.correct.alahmdy.data.quran.QuranModel
import com.correct.alahmdy.data.quran.Read
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER
import com.correct.alahmdy.helper.Constants.ITEM

class LastReadAdapter(
    private var list: List<Read>,
    private val listener: ClickListener
) : RecyclerView.Adapter<LastReadAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastReadAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.last_read_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LastReadAdapter.ViewHolder, position: Int) {
        holder.txt_name.text = list[position].english
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: List<Read>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_name: TextView = itemView.findViewById(R.id.txt_surah_name)

        init {
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable(ITEM, list[adapterPosition])
                bundle.putInt(ADAPTER, 1)
                listener.onItemClickListener(adapterPosition, bundle)
            }
        }
    }
}