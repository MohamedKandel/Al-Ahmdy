package com.correct.alahmdy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentDetectLocationBinding


class DetectLocationFragment : Fragment() {

    private lateinit var binding: FragmentDetectLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetectLocationBinding.inflate(inflater,container,false)
        return binding.root
    }
}