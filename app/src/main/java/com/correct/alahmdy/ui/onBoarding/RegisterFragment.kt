package com.correct.alahmdy.ui.onBoarding

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentRegisterBinding
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.room.PrayDB
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var listener: FragmentChangeListener
    private lateinit var prayDB: PrayDB

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
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        prayDB = PrayDB.getDBInstance(requireContext())

        binding.txtUsername.imeOptions = EditorInfo.IME_ACTION_DONE

        binding.btnDone.setOnClickListener {
            registerUser()
        }

        binding.txtUsername.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                registerUser()
            }
            true
        }

        return binding.root
    }

    private fun registerUser() {
        val name = binding.txtUsername.text.toString()
        if (name.isNotEmpty()) {
            Log.v("Done button", "Hello $name")
            lifecycleScope.launch {
                prayDB.userDao().updateUsername(1, name)
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }
}