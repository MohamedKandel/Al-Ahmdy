package com.correct.alahmdy.ui.home

import AdOnsAdapter
import android.Manifest
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import com.correct.alahmdy.R
import com.correct.alahmdy.adapter.PrayAdapter
import com.correct.alahmdy.data.home.AdOnsModel
import com.correct.alahmdy.data.home.PrayingTimeModel
import com.correct.alahmdy.databinding.FragmentHomeBinding
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.ADAPTER
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.Constants.CITY
import com.correct.alahmdy.helper.Constants.MUTE
import com.correct.alahmdy.helper.FragmentChangeListener
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), ClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var prayList: MutableList<PrayingTimeModel>
    private lateinit var prayAdapter: PrayAdapter
    private lateinit var adOnsList: MutableList<AdOnsModel>
    private lateinit var adOnAdapter: AdOnsAdapter
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var dataStore: DataStorage
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
    }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        lifecycleScope.launch {
            binding.txtCity.text = dataStore.getString(requireContext(), CITY)
        }

        fillPrayingTime()
        fillAdOns()

        binding.adOnsRecyclerView.adapter = adOnAdapter
        binding.prayingTimeRecyclerView.adapter = prayAdapter

        return binding.root
    }

    private fun fillPrayingTime() {
        prayList.add(PrayingTimeModel("Fajr", "4:37", "AM", false))
        prayList.add(PrayingTimeModel("Shuruq", "5:56", "AM", true))
        prayList.add(PrayingTimeModel("Dahur", "12:50", "PM", false))
        prayList.add(PrayingTimeModel("Asr", "4:30", "PM", false))
        prayList.add(PrayingTimeModel("Maghrib", "6:25", "PM", false))
        prayList.add(PrayingTimeModel("Ishaa", "8:47", "PM", false))

        binding.qiamPray.apply {
            txtPrayName.text = "Qiam"
            txtPrayTime.text = "1:20"
            txtPrayAa.text = "AM"
            // fetch data from database if user mute Qiam or not
            var isMute = true
            if (isMute) {
                mutePrayIcon.setImageResource(R.drawable.mute_icon)
            } else {
                mutePrayIcon.setImageResource(R.drawable.sound_icon)
            }
            mutePrayIcon.setOnClickListener {
                // check on database value
                if (isMute) {
                    mutePrayIcon.setImageResource(R.drawable.sound_icon)
                } else {
                    mutePrayIcon.setImageResource(R.drawable.mute_icon)
                }
                isMute = !isMute
            }
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