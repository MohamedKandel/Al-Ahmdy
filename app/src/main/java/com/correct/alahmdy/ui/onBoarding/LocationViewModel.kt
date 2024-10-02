package com.correct.alahmdy.ui.onBoarding

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.correct.alahmdy.data.location.LocationResponse
import com.correct.alahmdy.helper.Constants.ERROR_API
import com.correct.alahmdy.helper.Constants.API_KEY
import com.correct.alahmdy.helper.Constants.REVERSE_LOCATION_BASE_URL
import com.correct.alahmdy.helper.decrypt
import com.correct.alahmdy.helper.generateKeyFromString
import com.correct.alahmdy.retrofit.APIService
import com.correct.alahmdy.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: LocationRepository = LocationRepository(
        RetrofitClient.getClient(REVERSE_LOCATION_BASE_URL)
            .create(APIService::class.java)
    )

    private val _locationResponse = MutableLiveData<LocationResponse>()
    val locationResponse: LiveData<LocationResponse> get() = _locationResponse

    fun getCountryName(lat: String, lon: String) = viewModelScope.launch {
        val token = API_KEY
        val key = "Key".generateKeyFromString()
        val iv = "1234567890abcdef".toByteArray(Charsets.UTF_8)
        val result = repo.getCountryName(lat,lon,"json", token.decrypt(key,iv))
        if (result.isSuccessful) {
            _locationResponse.postValue(result.body())
        } else {
            Log.e(ERROR_API, "Error in Location request ${result.code()}")
        }
    }
}