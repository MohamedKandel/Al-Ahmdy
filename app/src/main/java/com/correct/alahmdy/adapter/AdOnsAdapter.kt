import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.correct.alahmdy.R
import com.correct.alahmdy.data.home.AdOnsModel
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER

class AdOnsAdapter(
    private var list: List<AdOnsModel>,
    private val listener: ClickListener
) : RecyclerView.Adapter<AdOnsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdOnsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ad_ons_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdOnsAdapter.ViewHolder, position: Int) {
        val model = list[position]
        holder.img.setImageResource(model.image)
        holder.txt.text = model.title
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: List<AdOnsModel>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageButton>(R.id.btn)
        val txt = itemView.findViewById<TextView>(R.id.txt_title)

        init {
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(ADAPTER, 2)
                listener.onItemClickListener(adapterPosition, bundle)
            }

            img.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(ADAPTER, 2)
                listener.onItemClickListener(adapterPosition, bundle)
            }

            txt.setOnClickListener{
                val bundle = Bundle()
                bundle.putInt(ADAPTER, 2)
                listener.onItemClickListener(adapterPosition, bundle)
            }
        }
    }
}