import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.correct.alahmdy.R
import com.correct.alahmdy.data.quran.FilterModel
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER
import com.correct.alahmdy.helper.Constants.CLICKED

class FilterAdapter(
    private var list: List<FilterModel>,
    private val listener: ClickListener
) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filtering_quran_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) {
        holder.txt.text = list[position].filterType
        if (position == selectedPosition) {
            holder.txt.setBackgroundResource(R.drawable.filter_drawable)  // Selected item
        } else {
            holder.txt.setBackgroundResource(0)  // Non-selected items
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: List<FilterModel>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt: TextView = itemView.findViewById(R.id.txt_filter_by)

        init {

            txt.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)

                txt.setBackgroundResource(R.drawable.filter_drawable)
                val bundle = Bundle()
                bundle.putInt(ADAPTER, 2)
                bundle.putInt(CLICKED, list[selectedPosition].id)
                listener.onItemClickListener(selectedPosition, bundle)
            }

            itemView.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)

                val bundle = Bundle()
                bundle.putInt(ADAPTER, 2)
                bundle.putInt(CLICKED, list[selectedPosition].id)
                listener.onItemClickListener(selectedPosition, bundle)
            }
        }
    }
}