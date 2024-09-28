package com.correct.alahmdy.retrofit

import com.correct.alahmdy.data.location.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    // location is in this format (latitude,longitude)
    @GET("v1/reverse")
    suspend fun getCountryName(@Query("lat") lat: String,
                               @Query("lon") lon: String,
                               @Query("format") format: String,
                               @Query("key") apiKey: String) : Response<LocationResponse>
}