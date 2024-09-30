package com.correct.alahmdy.ui.quraan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentQuraanBinding

class QuranFragment : Fragment() {

    private lateinit var binding: FragmentQuraanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuraanBinding.inflate(inflater,container,false)
        return binding.root
    }
}