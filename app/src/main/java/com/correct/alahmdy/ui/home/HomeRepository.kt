package com.correct.alahmdy.ui.home

import com.correct.alahmdy.retrofit.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository(private val apiService: APIService) {
    suspend fun getPrayingTime(date: String, lat: String, lon: String, method: Int) =
        withContext(Dispatchers.IO) {
            apiService.prayingTime(date, lat, lon, method)
        }
}