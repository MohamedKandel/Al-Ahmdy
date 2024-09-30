package com.correct.alahmdy.data.home

data class PrayingTimeModel(
    val prayName: String,
    val prayTime: String,
    val prayTimeAA: String,
    var isMute: Int         // 0 for mute, 1 for un-mute and -1 for hide icon
)
