package com.correct.alahmdy.ui.quraan

import com.correct.alahmdy.retrofit.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuranRepository(private val apiService: APIService) {
    suspend fun getSurahNames() = withContext(Dispatchers.IO) {
        apiService.getSurahName()
    }
}