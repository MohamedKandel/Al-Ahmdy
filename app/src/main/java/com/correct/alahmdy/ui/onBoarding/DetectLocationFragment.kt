package com.correct.alahmdy.ui.onBoarding

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.data.location.LocationResponse
import com.correct.alahmdy.databinding.FragmentDetectLocationBinding
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.Constants.CITY
import com.correct.alahmdy.helper.Constants.COUNTRY
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.onBackPressed
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch

class DetectLocationFragment : Fragment() {

    private lateinit var binding: FragmentDetectLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsClient: SettingsClient
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private lateinit var dataStore: DataStorage
    private lateinit var viewModel: LocationViewModel
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
        listener.onFragmentChangeListener(R.id.detectLocationFragment)
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            // Permission is granted, now check GPS and get location
            checkGPSAndRequestLocation()
        } else {
            // Permission is denied
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted, check GPS
                checkGPSAndRequestLocation()
            }

            else -> {
                // Permission is not granted, request it
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun checkGPSAndRequestLocation() {
        // Create a LocationSettingsRequest builder with the location request
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        locationSettingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener { _ ->
                // GPS is already enabled, get the location
                getLocation()
            }
            .addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        // GPS is not enabled, show the dialog to enable it
                        e.startResolutionForResult(requireActivity(), 1001)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                        Log.e("GPS", "Error occurred: ${sendEx.message}")
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                lifecycleScope.launch {
                    if (dataStore.getString(requireContext(), COUNTRY).isNullOrEmpty()) {
                        getCountryName(latitude, longitude)
                    } else {
                        println(dataStore.getString(requireContext(), COUNTRY))
                        println(dataStore.getString(requireContext(), CITY))
                        findNavController().navigate(R.id.registerFragment)
                    }
                }
            } ?: run {
                // Handle case where location is null
                Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Log.e("Location", "Error: ${it.message}")
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
        binding = FragmentDetectLocationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        dataStore = DataStorage.getInstance(requireContext())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationSettingsClient = LocationServices.getSettingsClient(requireActivity())

        // Create a LocationRequest
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .build()



        binding.imgMosque.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.slide_up
            )
        )

        binding.btnLocateMe.setOnClickListener {
            // Check and request location permission
            if (isLocationEnabled()) {
                checkLocationPermission()
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

        onBackPressed {
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        // clear animation to prevent app slow
        binding.imgMosque.clearAnimation()
    }

    private fun getCountryName(latitude: Double, longitude: Double) {
        viewModel.getCountryName("$latitude", "$longitude")
        val observer = object : Observer<LocationResponse> {
            override fun onChanged(value: LocationResponse) {
                println(value.address.country)
                lifecycleScope.launch {
                    dataStore.putString(
                        requireContext(),
                        COUNTRY,
                        value.address.country
                    )
                    dataStore.putString(requireContext(), CITY, value.address.city)
                    findNavController().navigate(R.id.registerFragment)
                }
                viewModel.locationResponse.removeObserver(this)
            }
        }
        viewModel.locationResponse.observe(viewLifecycleOwner, observer)
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return false
    }

    /*private fun createLocationRequest() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000
        ).setMinUpdateIntervalMillis(5000).build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {

        }
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(
                        requireActivity(),
                        100
                    )
                } catch (sendEx: java.lang.Exception) {

                }
            }
        }
    }*/
    /* this code in onCreateView method
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        val locationLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    if (isLocationEnabled()) {
                        val result = fusedLocation.getCurrentLocation(
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                            CancellationTokenSource().token
                        )
                        result.addOnCompleteListener {
                            val location =
                                "Latitude : ${it.result.latitude}\t Longitude : ${it.result.longitude}"
                            Log.i("Location service mohamed", location)
                            getCountryName(
                                requireContext(),
                                it.result.latitude,
                                it.result.longitude
                            )?.let { it1 ->
                                Log.d(
                                    "Location service mohamed",
                                    it1
                                )
                            }
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please turn on location service",
                            Toast.LENGTH_SHORT
                        ).show()
                        createLocationRequest()
                    }
                }

                else -> {
                    Toast.makeText(requireContext(), "No location accessed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }*/
    /* get country name without API (Not working in all android devices)
    val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var countryName = ""
        try {
            // Get a list of addresses based on the latitude and longitude
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(latitude, longitude, 1) {
                    countryName = it[0].countryName
                }

            } else {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                // Check if any address was found
                if (!addresses.isNullOrEmpty()) {
                    countryName = addresses[0].countryName // Extract the country name
                } else {
                    countryName = ""
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            countryName = ""
            Log.e("Geocoder", "Error: ${e.message}")
            Toast.makeText(context, "Error retrieving country name", Toast.LENGTH_SHORT).show()
        }
        return countryName*/
}