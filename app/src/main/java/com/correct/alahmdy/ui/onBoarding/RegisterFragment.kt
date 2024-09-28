package com.correct.alahmdy.ui.onBoarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentRegisterBinding
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.FragmentChangeListener

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var listener: FragmentChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        } else {
            throw ClassCastException(CAST_ERROR)
        }
    }

    override fun onResume() {
        super.onResume()
        listener.onFragmentChangeListener(R.id.registerFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        return binding.root
    }
}