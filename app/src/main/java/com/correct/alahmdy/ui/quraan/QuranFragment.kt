package com.correct.alahmdy.ui.quraan

import FilterAdapter
import LastReadAdapter
import QuranAdapter
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.data.quran.FilterModel
import com.correct.alahmdy.data.quran.QuranModel
import com.correct.alahmdy.data.quran.SurahNamesResponse
import com.correct.alahmdy.databinding.FragmentQuraanBinding
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.Constants.CLICKED
import com.correct.alahmdy.helper.Constants.ITEM
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.onBackPressed
import kotlin.random.Random
import kotlin.random.nextInt

class QuranFragment : Fragment(), ClickListener {

    private lateinit var binding: FragmentQuraanBinding
    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var lastReadList: MutableList<QuranModel>
    private lateinit var lastReadAdapter: LastReadAdapter
    private lateinit var filterList: MutableList<FilterModel>
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var quranList: MutableList<QuranModel>
    private lateinit var quranAdapter: QuranAdapter
    private lateinit var viewModel: QuranViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            fragmentChangeListener = context
        } else {
            throw ClassCastException(CAST_ERROR)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        fragmentChangeListener.onFragmentChangeListener(R.id.quranFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuraanBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[QuranViewModel::class.java]

        lastReadList = mutableListOf()
        lastReadAdapter = LastReadAdapter(lastReadList, this)

        filterList = mutableListOf()
        filterAdapter = FilterAdapter(filterList, this)

        quranList = mutableListOf()
        quranAdapter = QuranAdapter(quranList, this)

        binding.lastReadRecyclerView.adapter = lastReadAdapter
        binding.filteringRecyclerView.adapter = filterAdapter
        binding.quranRecyclerView.adapter = quranAdapter

        fillQuran()
        fillFilter()

        binding.layoutHeader.apply {
            txtTitle.text = resources.getString(R.string.al_quraan)
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.homeFragment)
            }
        }

        onBackPressed {
            findNavController().navigate(R.id.homeFragment)
        }

        return binding.root
    }

    private fun fillLastRead() {
        // get data from database
        for (i in 0..4) {
            val random = Random.nextInt(0..114)
            val model = quranList[random]
            lastReadList.add(model)
        }
        lastReadAdapter.updateAdapter(lastReadList)
    }

    private fun fillFilter() {
        filterList.add(FilterModel(1, resources.getString(R.string.surah)))
        filterList.add(FilterModel(2, resources.getString(R.string.page)))
        filterList.add(FilterModel(3, resources.getString(R.string.juz)))
        filterList.add(FilterModel(4, resources.getString(R.string.hizb)))

        filterAdapter.updateAdapter(filterList)
    }

    private fun fillQuran() {
        viewModel.getSurahNames()
        val observer = object : Observer<SurahNamesResponse> {
            override fun onChanged(value: SurahNamesResponse) {
                quranList.addAll(value.data)
                quranAdapter.updateAdapter(quranList)
                fillLastRead()
                viewModel.surahsNamesResponse.removeObserver(this)
            }
        }
        viewModel.surahsNamesResponse.observe(viewLifecycleOwner, observer)
    }

    override fun onItemClickListener(position: Int, extras: Bundle?) {
        val adapterID = extras?.getInt(ADAPTER)
        when (adapterID) {
            1 -> {
                // last read adapter clicked
            }

            2 -> {
                // filter adapter clicked
                Log.i("Adapter ID mohamed", "${extras.getInt(CLICKED)}")
            }

            3 -> {
                // quran adapter clicked
                val model = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    extras.getParcelable(ITEM,QuranModel::class.java)
                } else {
                    extras.getParcelable(ITEM)
                }
                Log.i("Adapter ID mohamed", "${model?.number}\t${model?.name}\t${model?.englishName}")
            }
        }
    }

    override fun onItemLongClickListener(position: Int, extras: Bundle?) {

    }
}