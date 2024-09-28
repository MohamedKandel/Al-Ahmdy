package com.correct.alahmdy.ui.onBoarding

import com.correct.alahmdy.retrofit.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepository(private val apiService: APIService) {
    suspend fun getCountryName(lat: String, lon: String, format: String, key: String) = withContext(Dispatchers.IO) {
        apiService.getCountryName(lat,lon,format,key)
    }
}