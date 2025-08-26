package com.example.weatherforecastapp


sealed class Screens(val route: String) {
    object LocationSelection : Screens("locationSelection")
    object ForecastData : Screens("forecastData/{city}") {
        fun route(city: String) = "forecastData/$city"
    }
    object ForecastDetailedData : Screens("forecastDetailedData/{city}/{dt}") {
        fun route(city: String, dt: Long) = "forecastDetailedData/$city/$dt"
    }
}
