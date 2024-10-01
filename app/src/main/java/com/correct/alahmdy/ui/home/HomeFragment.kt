package com.correct.alahmdy.ui.home

import AdOnsAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.adapter.PrayAdapter
import com.correct.alahmdy.data.home.AdOnsModel
import com.correct.alahmdy.data.home.PrayingTimeModel
import com.correct.alahmdy.data.pray.PrayTimeResponse
import com.correct.alahmdy.databinding.FragmentHomeBinding
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.Constants.CITY
import com.correct.alahmdy.helper.Constants.LATITUDE
import com.correct.alahmdy.helper.Constants.LONGITUDE
import com.correct.alahmdy.helper.Constants.MUTE
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.getAa
import com.correct.alahmdy.helper.getTime
import com.correct.alahmdy.helper.onBackPressed
import com.correct.alahmdy.helper.reformat24HourTime
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.min

class HomeFragment : Fragment(), ClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var prayList: MutableList<PrayingTimeModel>
    private lateinit var prayAdapter: PrayAdapter
    private lateinit var adOnsList: MutableList<AdOnsModel>
    private lateinit var adOnAdapter: AdOnsAdapter
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var dataStore: DataStorage
    private lateinit var viewModel: HomeViewModel
    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private val notificationLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                // permission is granted

            } else {
                // permission is denied
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.notification_required),
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            )
                    == PackageManager.PERMISSION_GRANTED -> {
                // permission granted
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                // show an explanation to the user
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.notification_sorry),
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }

            else -> {
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            changeListener = context
        } else {
            throw ClassCastException(CAST_ERROR)
        }
    }

    override fun onResume() {
        super.onResume()
        changeListener.onFragmentChangeListener(R.id.homeFragment)
        binding.txtTime.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun onPause() {
        super.onPause()
        binding.txtTime.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        prayList = mutableListOf()
        prayAdapter = PrayAdapter(prayList, this)
        adOnsList = mutableListOf()
        adOnAdapter = AdOnsAdapter(adOnsList, this)
        dataStore = DataStorage.getInstance(requireContext())
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        lifecycleScope.launch {
            binding.txtCity.text = dataStore.getString(requireContext(), CITY)
        }

        val tempTime = "18:40".reformat24HourTime()
        Log.e("Reformat 24 hour time", tempTime)
        Log.e("Reformat 24 hour time", tempTime.getAa())
        Log.e("Reformat 24 hour time", tempTime.getTime())

        fillPrayingTime()
        fillAdOns()

        val list = temp()
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            println("${binding.txtTime.text} ${binding.txtAa.text}")
            val current = parseTime("${binding.txtTime.text} ${binding.txtAa.text}")
            val (nearestNextTime, differenceInMinutes) = getNearestNextTimeAndDifference(
                current,
                list
            )

            // Print the nearest next time (formatted back to "hh:mm a")
            nearestNextTime?.let {
                val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                println("Nearest next time: ${outputFormat.format(it.time)}")
                println("Difference: ${formatDifference(differenceInMinutes)}")
                println("Next pray : ${getPrayNameByTime(outputFormat.format(it.time))}")
                binding.txtRemaining.text =
                    "${getPrayNameByTime(outputFormat.format(it.time))} ${
                        formatDifference(differenceInMinutes)
                    }"
            } ?: println("No times found.")
        }

        //binding.txtTime.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        binding.adOnsRecyclerView.adapter = adOnAdapter
        binding.prayingTimeRecyclerView.adapter = prayAdapter

        onBackPressed {
            requireActivity().finish()
        }

        return binding.root
    }

    private fun formatDifference(minutes: Long): String {
        val hours = getHours(minutes.toInt())
        val minutes = getMin(minutes.toInt())
        return if (hours == 0) {
            "$minutes minutes left"
        } else {
            "$hours hours and $minutes minutes left"
        }
    }

    private fun temp(): List<Calendar> {
        val prayTime = mutableListOf<String>()
        for (pray in prayList) {
            prayTime.add("${pray.prayTime} ${pray.prayTimeAA}")
        }
        val fajr = parseTime(prayTime[0])
        val duhr = parseTime(prayTime[2])
        val asr = parseTime(prayTime[3])
        val maghrib = parseTime(prayTime[4])
        val ishaa = parseTime(prayTime[5])

        return listOfNotNull(fajr, duhr, asr, maghrib, ishaa)
    }

    private fun getNearestNextTimeAndDifference(
        currentTime: Calendar,
        times: List<Calendar>
    ): Pair<Calendar?, Long> {
        // Filter out times that are before or equal to the current time
        val futureTimes = times.filter { it.after(currentTime) }

        // Get the nearest future time or the first time in the list if no future time is found
        val nearestNextTime = futureTimes.minByOrNull { it.timeInMillis - currentTime.timeInMillis }
            ?: times.firstOrNull()

        // Calculate the difference in minutes
        val differenceInMinutes = nearestNextTime?.let {
            if (it.before(currentTime)) {
                // If the nearest next time is before or equal to current time, it means it is on the next day
                val tempCalendar = Calendar.getInstance().apply {
                    timeInMillis = it.timeInMillis
                    add(Calendar.DAY_OF_YEAR, 1)  // Add 1 day for wrap-around to next day
                }
                val diffInMillis = tempCalendar.timeInMillis - currentTime.timeInMillis
                diffInMillis / (60 * 1000)  // Convert milliseconds to minutes
            } else {
                val diffInMillis = it.timeInMillis - currentTime.timeInMillis
                diffInMillis / (60 * 1000)  // Convert milliseconds to minutes
            }
        } ?: 0L  // If nearestNextTime is null, return 0

        return Pair(nearestNextTime, differenceInMinutes)
    }

    private fun parseTime(timeString: String): Calendar {
        return try {
            val inputFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
            val date = inputFormat.parse(timeString)
            Calendar.getInstance().apply {
                time = date!!
                set(Calendar.SECOND, 0)  // Clear the seconds field for accurate comparison
                set(Calendar.MILLISECOND, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Calendar.getInstance()
        }
    }

    private fun getPrayNameByTime(prayTime: String): String {
        for (pray in prayList) {
            val temp = "${pray.prayTime} ${pray.prayTimeAA}"
            if (prayTime.equals(temp, true)) {
                return pray.prayName
            }
        }
        return ""
    }

    private fun getHours(minutes: Int): Int {
        var count = 0
        var newMinutes = minutes
        while (newMinutes >= 60) {
            newMinutes = newMinutes.minus(60)
            count++
        }
        return count
    }

    private fun getMin(minutes: Int): Int = minutes % 60

    // fetch data from API here
    private fun fillPrayingTime() {
        prayList.add(PrayingTimeModel("Fajr", "04:37", "AM", 1))     //0
        prayList.add(PrayingTimeModel("Shuruq", "05:56", "AM", -1))    //1
        prayList.add(PrayingTimeModel("Dahur", "12:50", "PM", 1))   //2
        prayList.add(PrayingTimeModel("Asr", "04:35", "PM", 1))      //3
        prayList.add(PrayingTimeModel("Maghrib", "06:25", "PM", 0))  //4
        prayList.add(PrayingTimeModel("Ishaa", "08:47", "PM", 1))    //5

        binding.qiamPray.apply {
            txtPrayName.text = "Qiam"
            txtPrayTime.text = "1:20"
            txtPrayAa.text = "AM"
            // fetch data from database if user mute Qiam or not
            var isMute = 0
            if (isMute == 0) {
                mutePrayIcon.setImageResource(R.drawable.mute_icon)
            } else {
                mutePrayIcon.setImageResource(R.drawable.sound_icon)
            }
            mutePrayIcon.setOnClickListener {
                // check on database value
                if (isMute == 0) {
                    mutePrayIcon.setImageResource(R.drawable.sound_icon)
                    isMute = 1
                } else {
                    mutePrayIcon.setImageResource(R.drawable.mute_icon)
                    isMute = 0
                }
            }
        }
        //////////////////////////////////////////////////////////////////
        val d = Date()
        val date = SimpleDateFormat("dd-MM-yyyy").format(d)
        Log.v("Reformat 24 hour time", date)
        lifecycleScope.launch {
            val lat = dataStore.getString(requireContext(), LATITUDE) ?: ""
            val long = dataStore.getString(requireContext(), LONGITUDE) ?: ""
            viewModel.getPrayTime(date, lat, long, 5)
            val observer = object : Observer<PrayTimeResponse> {
                override fun onChanged(value: PrayTimeResponse) {
                    val hijri = "${value.data.date.hijri.day} ${value.data.date.hijri.month.en} ${value.data.date.hijri.year}"
                    binding.txtHijriDate.text = hijri

                    viewModel.prayTimeResponse.removeObserver(this)
                }
            }
            viewModel.prayTimeResponse.observe(viewLifecycleOwner,observer)
        }

        prayAdapter.updateAdapter(prayList)
    }

    private fun fillAdOns() {
        adOnsList.add(AdOnsModel(R.drawable.sebha, resources.getString(R.string.tasbeh)))
        adOnsList.add(AdOnsModel(R.drawable.doaa, resources.getString(R.string.duaa)))
        adOnsList.add(AdOnsModel(R.drawable.azkar, resources.getString(R.string.azkar)))
        adOnsList.add(AdOnsModel(R.drawable.hadith, resources.getString(R.string.hadith)))

        adOnAdapter.updateAdapter(adOnsList)
    }

    override fun onItemClickListener(position: Int, extras: Bundle?) {
        if (extras != null) {
            Log.i("Item clicked", "${extras.getInt(ADAPTER)}")
            when (extras.getInt(ADAPTER)) {
                1 -> {
                    // pray
                    val isMute = extras.getBoolean(MUTE)
                    Log.d("Is mute mohamed", "$isMute")
                }

                2 -> {
                    // ad ons
                    Log.v("Item clicked position", "$position")
                }

                else -> {
                    // default
                }
            }
        }
    }

    override fun onItemLongClickListener(position: Int, extras: Bundle?) {

    }
}