package com.correct.alahmdy.ui.azkar

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentAzkarBinding
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.onBackPressed

class AzkarFragment : Fragment() {

    private lateinit var binding: FragmentAzkarBinding
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

    override fun onResume() {
        super.onResume()
        changeListener.onFragmentChangeListener(R.id.azkarFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAzkarBinding.inflate(inflater,container,false)

        binding.headerLayout.txtTitle.text = resources.getString(R.string.azkar)
        binding.headerLayout.btnBack.setOnClickListener {
            findNavController().navigate(R.id.azkarIndexingFragment)
        }

        onBackPressed {
            findNavController().navigate(R.id.azkarIndexingFragment)
        }

        return binding.root
    }
}