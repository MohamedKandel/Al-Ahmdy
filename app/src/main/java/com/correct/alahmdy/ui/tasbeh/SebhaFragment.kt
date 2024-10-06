package com.correct.alahmdy.ui.tasbeh

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.data.user.Tasbeh
import com.correct.alahmdy.databinding.FragmentSebhaBinding
import com.correct.alahmdy.helper.AudioUtils
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.Constants.IS_SOUND
import com.correct.alahmdy.helper.Constants.IS_VIBRATE
import com.correct.alahmdy.helper.Constants.TOTAL_COUNT
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.onBackPressed
import com.correct.alahmdy.helper.vibrate
import com.correct.alahmdy.room.PrayDB
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class SebhaFragment : Fragment() {

    private lateinit var binding: FragmentSebhaBinding
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var prayDB: PrayDB
    private lateinit var list: MutableList<Tasbeh>
    private lateinit var dataStore: DataStorage
    private lateinit var audioUtils: AudioUtils
    private var count = 0
    private var isVibrateOn = true
    private var isSoundOn = true
    private var firstRandom = 0
    private var secondRandom = 0
    private var totalCount = 0

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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSebhaBinding.inflate(inflater, container, false)
        prayDB = PrayDB.getDBInstance(requireContext())
        dataStore = DataStorage.getInstance(requireContext())
        audioUtils = AudioUtils.getInstance()

        list = mutableListOf()

        binding.txtCount.text = "$count"

        binding.headerLayout.apply {
            txtTitle.text = resources.getString(R.string.tasbeh)
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.tasbehIndexingFragment)
            }
        }

        onBackPressed {
            findNavController().navigate(R.id.tasbehIndexingFragment)
        }

        lifecycleScope.launch {
            // fill with all tasbeh
            list.addAll(prayDB.tasbehDao().getAll())
            totalCount = dataStore.getInt(requireContext(), TOTAL_COUNT) ?: 0

            binding.txtTotal.text = "${resources.getString(R.string.total_tasbeh)} $totalCount"

            if (dataStore.getBoolean(requireContext(), IS_VIBRATE) == null) {
                dataStore.putBoolean(requireContext(), IS_VIBRATE, true)
            }
            if (dataStore.getBoolean(requireContext(), IS_SOUND) == null) {
                dataStore.putBoolean(requireContext(), IS_SOUND, true)
            }

            isVibrateOn = dataStore.getBoolean(requireContext(), IS_VIBRATE) ?: true
            if (!isVibrateOn) {
                binding.vibrateBtn.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.alphaBlack
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
            } else {
                binding.vibrateBtn.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }
            isSoundOn = dataStore.getBoolean(requireContext(), IS_SOUND) ?: true
            if (!isSoundOn) {
                binding.soundBtn.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.alphaBlack
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
            } else {
                binding.soundBtn.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }


            // set text with zekr
            firstRandom = generateRandomNumber()
            binding.txtZekr.text = list[firstRandom].tasbeh

            binding.count.setOnClickListener {
                binding.count.isEnabled = false
                Handler(Looper.getMainLooper()).postDelayed({
                    lifecycleScope.launch {
                        val isVibrateOn =
                            dataStore.getBoolean(requireContext(), IS_VIBRATE) ?: false
                        val isSoundOn = dataStore.getBoolean(requireContext(), IS_SOUND) ?: false

                        if (isVibrateOn) {
                            vibrate(50)
                        }
                        if (isSoundOn) {
                            // play sound
                            audioUtils.playAudio(requireContext(), R.raw.tasbeh_sound)
//                            audioUtils.releaseMedia()
                        }
                        count++
                        totalCount++
                        binding.txtTotal.text = "${resources.getString(R.string.total_tasbeh)} $totalCount"
                        dataStore.putInt(requireContext(), TOTAL_COUNT, totalCount)
                        binding.txtCount.text = "$count"
                    }
                    binding.count.isEnabled = true
                }, 700)
            }

            binding.reloadBtn.setOnClickListener {
                secondRandom = generateRandomNumber()
                while (secondRandom == firstRandom) {
                    secondRandom = generateRandomNumber()
                }
                firstRandom = secondRandom
                binding.txtZekr.text = list[secondRandom].tasbeh
                count = 0
                binding.txtCount.text = "$count"
            }

            binding.vibrateBtn.setOnClickListener {
                lifecycleScope.launch {
                    isVibrateOn = dataStore.getBoolean(requireContext(), IS_VIBRATE) ?: true
                    dataStore.putBoolean(requireContext(), IS_VIBRATE, !isVibrateOn)

                    if (!isVibrateOn) {
                        binding.vibrateBtn.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        binding.vibrateBtn.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.alphaBlack
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    }
                }
            }

            binding.soundBtn.setOnClickListener {
                lifecycleScope.launch {
                    isSoundOn = dataStore.getBoolean(requireContext(), IS_SOUND) ?: true
                    dataStore.putBoolean(requireContext(), IS_SOUND, !isSoundOn)

                    if (!isSoundOn) {
                        binding.soundBtn.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        binding.soundBtn.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.alphaBlack
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    }
                }
                Log.v("Vibrate and sound mohamed", "${!isSoundOn}")
            }

        }



        return binding.root
    }

    private fun generateRandomNumber() = Random.nextInt(0, list.size)
}