package com.correct.alahmdy.ui.onBoarding

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentSplashBinding
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.Constants.CITY
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.onBackPressed
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    private val TAG: String = "SplashFragment mohamed"
    private lateinit var binding: FragmentSplashBinding
    private var milliFinished: Long = 0
    private var countDownTimer: CountDownTimer? = null
    private lateinit var listener: FragmentChangeListener
    private lateinit var dataStore: DataStorage

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        } else {
            throw ClassCastException(CAST_ERROR)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        listener.onFragmentChangeListener(R.id.splashFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        dataStore = DataStorage.getInstance(requireContext())
        startSplash()

        onBackPressed {
            requireActivity().finish()
        }

        binding.root.keepScreenOn = true

        return binding.root
    }

    private fun startSplash() {
        countDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                milliFinished = millisUntilFinished
                Log.i(TAG, "onTick: millisUntilFinished=$millisUntilFinished")
            }

            override fun onFinish() {
                // navigate to onBoarding
                Log.i(TAG, "onFinish: ${milliFinished}")
                lifecycleScope.launch {
                    val city = dataStore.getString(requireContext(),CITY)?: ""
                    if (city.isEmpty()) {
                        findNavController().navigate(R.id.detectLocationFragment)
                    } else {
                        findNavController().navigate(R.id.homeFragment)
                    }
                }
            }
        }.start()
    }

    override fun onDetach() {
        super.onDetach()
        countDownTimer?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }
}