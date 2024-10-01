import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.correct.alahmdy.R
import com.correct.alahmdy.data.quran.QuranModel
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER
import com.correct.alahmdy.helper.Constants.ITEM
import com.correct.alahmdy.helper.hide
import com.correct.alahmdy.helper.show

class QuranAdapter(
    private var list: List<QuranModel>,
    private val listener: ClickListener
) : RecyclerView.Adapter<QuranAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quran_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranAdapter.ViewHolder, position: Int) {
        val model = list[position]
        holder.txt_id.text = "${model.number}"
        holder.txt_ar.text = model.name
        holder.txt_en.text = model.englishName
        if (position == (list.size - 1)) {
            holder.line.hide()
        } else {
            holder.line.show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: List<QuranModel>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_id: TextView = itemView.findViewById(R.id.txt_number)
        val txt_en: TextView = itemView.findViewById(R.id.txt_en)
        val txt_ar: TextView = itemView.findViewById(R.id.txt_ar)
        val line: View = itemView.findViewById(R.id.line)

        init {
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(ADAPTER, 3)
                bundle.putParcelable(ITEM, list[adapterPosition])
                listener.onItemClickListener(adapterPosition, bundle)
            }
        }
    }
}