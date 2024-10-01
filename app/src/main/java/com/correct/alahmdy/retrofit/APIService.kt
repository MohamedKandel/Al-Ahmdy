package com.correct.alahmdy.retrofit

import com.correct.alahmdy.data.location.LocationResponse
import com.correct.alahmdy.data.pray.PrayTimeResponse
import com.correct.alahmdy.data.quran.SurahNamesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    // location is in this format (latitude,longitude)
    @GET("v1/reverse")
    suspend fun getCountryName(@Query("lat") lat: String,
                               @Query("lon") lon: String,
                               @Query("format") format: String,
                               @Query("key") apiKey: String) : Response<LocationResponse>

    @GET("v1/surah")
    suspend fun getSurahName(): Response<SurahNamesResponse>

    @GET("v1/timings/{date}")
    suspend fun prayingTime(@Path("date") date: String,
                            @Query("latitude") lat: String,
                            @Query("longitude") lon: String,
                            @Query("method") method: Int): Response<PrayTimeResponse>
}