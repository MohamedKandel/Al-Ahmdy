package com.correct.alahmdy.ui.duaa

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentDoaaBinding
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.FragmentChangeListener

class DoaaFragment : Fragment() {

    private lateinit var binding: FragmentDoaaBinding
    private lateinit var changeListener: FragmentChangeListener

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDoaaBinding.inflate(inflater,container,false)
        return binding.root
    }

}