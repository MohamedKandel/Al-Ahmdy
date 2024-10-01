package com.correct.alahmdy.data.quran

data class SurahNamesResponse(val code: Int, val status: String,
    val data: List<QuranModel>)
