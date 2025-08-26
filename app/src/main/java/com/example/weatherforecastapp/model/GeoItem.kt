package com.example.weatherforecastapp.model


data class GeoItem(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String?,
    val state: String?
) {
    fun displayName(): String = listOfNotNull(name, state, country).joinToString(", ")
}