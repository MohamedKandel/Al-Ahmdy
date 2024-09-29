package com.correct.alahmdy.ui.qibla

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.correct.alahmdy.R
import com.correct.alahmdy.databinding.FragmentQiblaBinding
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.FragmentChangeListener
import com.ib.qiblafinder.QiblaDegreeListener
import io.github.derysudrajat.compassqibla.CompassQibla

class QiblaFragment : Fragment() {

    private lateinit var binding: FragmentQiblaBinding
    private lateinit var listener: FragmentChangeListener

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
        listener.onFragmentChangeListener(R.id.qiblaFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQiblaBinding.inflate(inflater, container, false)


        binding.qibla.degreeListener = object : QiblaDegreeListener {
            @SuppressLint("SetTextI18n")
            override fun onDegreeChange(degree: Float) {
                Log.d("Degree listener", "$degree")
                binding.txtAngle.text = "$degree°"
            }
        }

        return binding.root
    }
}