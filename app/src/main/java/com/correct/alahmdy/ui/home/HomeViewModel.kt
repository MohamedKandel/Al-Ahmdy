package com.correct.alahmdy.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.correct.alahmdy.data.pray.PrayTimeResponse
import com.correct.alahmdy.helper.Constants.PRAY_TIME_BASE_URL
import com.correct.alahmdy.helper.onDataFetched
import com.correct.alahmdy.retrofit.APIService
import com.correct.alahmdy.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: HomeRepository = HomeRepository(
        RetrofitClient
            .getClient(PRAY_TIME_BASE_URL).create(APIService::class.java)
    )

    private val _prayTimeResponse = MutableLiveData<PrayTimeResponse>()
    val prayTimeResponse: LiveData<PrayTimeResponse> get() = _prayTimeResponse

    fun getPrayTime(date: String, lat: String, long: String, method: Int, listener: onDataFetched<PrayTimeResponse>) = viewModelScope.launch {
        val result = repo.getPrayingTime(date, lat, long, method)
        if (result.isSuccessful) {
            _prayTimeResponse.postValue(result.body())
            result.body()?.let {
                listener.onResultFetchedSuccessfull(it)
            }
        }
    }
}