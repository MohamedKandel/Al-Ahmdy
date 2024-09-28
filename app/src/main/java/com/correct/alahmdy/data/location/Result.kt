package com.correct.alahmdy.data.location

data class Result(
    val components: Components,
    val confidence: Int,
    val formatted: String
)
