package com.correct.alahmdy.data.location

data class Address(
    val city: String?,
    val country: String,
    val country_code: String,
    val state: String,
    val house_number: String?,
    val postcode: String?,
    val road: String?
)