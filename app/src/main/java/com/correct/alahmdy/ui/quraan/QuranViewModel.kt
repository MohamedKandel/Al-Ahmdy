package com.correct.alahmdy.ui.quraan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.correct.alahmdy.data.quran.SurahNamesResponse
import com.correct.alahmdy.helper.Constants.SURAH_META_DATA_BASE_URL
import com.correct.alahmdy.retrofit.APIService
import com.correct.alahmdy.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class QuranViewModel(application: Application): AndroidViewModel(application) {
    private val repo: QuranRepository = QuranRepository(RetrofitClient
        .getClient(SURAH_META_DATA_BASE_URL).create(APIService::class.java))

    private val _surahsNamesResponse = MutableLiveData<SurahNamesResponse>()
    val surahsNamesResponse: LiveData<SurahNamesResponse> get() = _surahsNamesResponse

    fun getSurahNames() = viewModelScope.launch {
        val result = repo.getSurahNames()
        if (result.isSuccessful) {
            _surahsNamesResponse.postValue(result.body())
        }
    }
}