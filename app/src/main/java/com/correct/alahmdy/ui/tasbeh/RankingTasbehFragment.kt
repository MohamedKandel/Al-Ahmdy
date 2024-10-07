package com.correct.alahmdy.ui.tasbeh

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.adapter.RankingAdapter
import com.correct.alahmdy.data.Ranking
import com.correct.alahmdy.databinding.FragmentRankingTasbehBinding
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.onBackPressed

class RankingTasbehFragment : Fragment(), ClickListener {

    private lateinit var binding: FragmentRankingTasbehBinding
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var list:MutableList<Ranking>
    private lateinit var rankAdapter: RankingAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            changeListener = context
        } else {
            throw ClassCastException(CAST_ERROR)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        changeListener.onFragmentChangeListener(R.id.rankingTasbehFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRankingTasbehBinding.inflate(inflater,container,false)

        list = mutableListOf()
        rankAdapter = RankingAdapter(list,this)
        binding.rankingRecyclerView.adapter = rankAdapter

        fillRank()

        binding.headerLayout.txtTitle.text = resources.getString(R.string.list_tasbeh)
        binding.headerLayout.btnBack.setOnClickListener {
            findNavController().navigate(R.id.sebhaFragment)
        }

        onBackPressed {
            findNavController().navigate(R.id.sebhaFragment)
        }

        return binding.root
    }

    private fun fillRank() {
        list.add(Ranking(1,"Adam Hossam",2000,R.drawable.cup_icon))
        list.add(Ranking(2,"Ahmed Aly",1800,R.drawable.filled_star_icon))
        list.add(Ranking(3,"Hatem sayed",1500,R.drawable.filled_star_icon))
        list.add(Ranking(4,"Karim Hany",1200,R.drawable.filled_star_icon))
        list.add(Ranking(5,"Khaled Mohamed",1000,R.drawable.empty_star_icon))
        list.add(Ranking(6,"Shams",800,R.drawable.empty_star_icon))
        list.add(Ranking(7,"Sohila hossam",500,R.drawable.empty_star_icon))

        rankAdapter.updateAdapter(list)
    }

    override fun onItemClickListener(position: Int, extras: Bundle?) {
        val adapter = extras?.getInt(ADAPTER)
        Log.v("Adapter view type", "$adapter")
    }

    override fun onItemLongClickListener(position: Int, extras: Bundle?) {

    }
}