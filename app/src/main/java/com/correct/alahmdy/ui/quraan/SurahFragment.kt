package com.correct.alahmdy.ui.quraan

import SurahAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentSurahBinding
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.hide
import com.correct.alahmdy.helper.show

class SurahFragment : Fragment(), ClickListener {

    private lateinit var binding: FragmentSurahBinding
    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var list: MutableList<Int>
    private lateinit var adapter: SurahAdapter
    private var isScrolling = false  // Flag to control auto-scrolling
    private var isBtmDisplayed = true
    private val scrollHandler = Handler(Looper.getMainLooper())

    private val scrollRunnable = object : Runnable {
        override fun run() {
            if (isScrolling) {
                // Small scroll amount (adjust as necessary)
                val scrollAmount = 10
                binding.surahRecyclerView.smoothScrollBy(0, scrollAmount, LinearInterpolator(), 50)

                // Continue scrolling after a short delay
                scrollHandler.postDelayed(this, 50)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            fragmentChangeListener = context
        } else {
            throw ClassCastException(CAST_ERROR)
        }
    }

    override fun onResume() {
        super.onResume()
        fragmentChangeListener.onFragmentChangeListener(R.id.surahFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSurahBinding.inflate(inflater, container, false)
        list = mutableListOf(
            R.drawable.img_quran,
            R.drawable.img_quran_2,
            R.drawable.img_quran_3
        )

        adapter = SurahAdapter(list, this)

        binding.surahRecyclerView.adapter = adapter

        binding.autoScrollLayout.setOnClickListener {
            if (isScrolling) {
                // stop
                binding.bottomBar.show()
                binding.headerLayout.root.show()

                stopAutoScroll()
            } else {
                binding.bottomBar.hide()
                binding.headerLayout.root.hide()

                // start
                binding.surahRecyclerView.postDelayed({
                    startAutoScroll()
                }, 1000)
            }
            isBtmDisplayed = isScrolling
        }


        return binding.root
    }

    private fun startAutoScroll() {
        isScrolling = true
        scrollHandler.post(scrollRunnable)
    }

    private fun stopAutoScroll() {
        isScrolling = false
        scrollHandler.removeCallbacks(scrollRunnable)
    }

    override fun onItemClickListener(position: Int, extras: Bundle?) {
        if (isBtmDisplayed) {
            binding.bottomBar.hide()
            binding.headerLayout.root.hide()
        } else {
            binding.bottomBar.show()
            binding.headerLayout.root.show()
        }
        isBtmDisplayed = !isBtmDisplayed
    }

    override fun onItemLongClickListener(position: Int, extras: Bundle?) {

    }
}