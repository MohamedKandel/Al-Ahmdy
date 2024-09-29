package com.correct.alahmdy.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.correct.alahmdy.R
import com.correct.alahmdy.data.home.PrayingTimeModel
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER
import com.correct.alahmdy.helper.Constants.MUTE

class PrayAdapter(
    private var list: List<PrayingTimeModel>,
    private val listener: ClickListener
) : RecyclerView.Adapter<PrayAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.praying_time_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrayAdapter.ViewHolder, position: Int) {
        val model = list[position]
        holder.txt_pray_name.text = model.prayName
        holder.txt_pray_time.text = model.prayTime
        holder.txt_pray_time_aa.text = model.prayTimeAA
        if (model.isMute) {
            holder.isMute_btn.setImageResource(R.drawable.mute_icon)
        } else {
            holder.isMute_btn.setImageResource(R.drawable.sound_icon)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: List<PrayingTimeModel>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_pray_name = itemView.findViewById<TextView>(R.id.txt_pray_name)
        val txt_pray_time = itemView.findViewById<TextView>(R.id.txt_pray_time)
        val txt_pray_time_aa = itemView.findViewById<TextView>(R.id.txt_pray_aa)
        val isMute_btn = itemView.findViewById<ImageButton>(R.id.mute_pray_icon)

        init {
            isMute_btn.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(ADAPTER,1)
                if (list[adapterPosition].isMute) {
                    isMute_btn.setImageResource(R.drawable.sound_icon)
                    list[adapterPosition].isMute = false
                    bundle.putBoolean(MUTE,false)
                } else {
                    isMute_btn.setImageResource(R.drawable.mute_icon)
                    list[adapterPosition].isMute = true
                    bundle.putBoolean(MUTE,true)
                }
                listener.onItemClickListener(adapterPosition, bundle)
            }
        }
    }
}