package com.correct.alahmdy.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.correct.alahmdy.R
import com.correct.alahmdy.data.Ranking
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER

class RankingAdapter(
    private var list: List<Ranking>,
    private val listener: ClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val firstLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.first_ranking_item, parent, false)
            return FirstRankViewHolder(firstLayout)
        } else {
            val secondLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.ranking_list_item, parent, false)
            return SecondRankViewHolder(secondLayout)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list:MutableList<Ranking>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is FirstRankViewHolder) {
            holder.setViews(model.rank, model.name, model.score, model.icon)
        } else {
            (holder as SecondRankViewHolder).setViews(
                model.rank,
                model.name,
                model.score,
                model.icon
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].rank == 1) {
            1
        } else {
            2
        }
//        return list[position].viewType
    }

    inner class FirstRankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtView_rank: TextView = itemView.findViewById(R.id.txt_rank)
        val txtView_name: TextView = itemView.findViewById(R.id.txt_name)
        val txtView_points: TextView = itemView.findViewById(R.id.txt_score)
        val icon: ImageView = itemView.findViewById(R.id.cup_icon)

        init {
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(ADAPTER,1)
                listener.onItemClickListener(adapterPosition, bundle)
            }
        }

        fun setViews(rank: Int, name: String, points: Int, icon: Int) {
            txtView_rank.text = "$rank"
            txtView_name.text = name
            txtView_points.text = "$points"
            this.icon.setImageResource(icon)
        }
    }

    inner class SecondRankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtView_rank: TextView = itemView.findViewById(R.id.txt_rank)
        val txtView_name: TextView = itemView.findViewById(R.id.txt_name)
        val txtView_points: TextView = itemView.findViewById(R.id.txt_score)
        val icon: ImageView = itemView.findViewById(R.id.cup_icon)

        init {
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(ADAPTER,2)
                listener.onItemClickListener(adapterPosition, bundle)
            }
        }

        fun setViews(rank: Int, name: String, points: Int, icon: Int) {
            txtView_rank.text = "$rank"
            txtView_name.text = name
            txtView_points.text = "$points"
            this.icon.setImageResource(icon)
        }
    }

}