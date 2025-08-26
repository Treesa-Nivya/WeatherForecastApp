package com.example.weatherforecastapp.model


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>

)

data class ForecastItem(
    val dt: Long,
    val main: MainData,
    val weather: List<WeatherDesc>,
    val wind: Wind?,
    val visibility: Int?,
    val pop: Double?,
    @SerializedName("dt_txt") val dtTxt: String
)

data class MainData(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val pressure: Int,
    @SerializedName("sea_level") val seaLevel: Int?,
    @SerializedName("grnd_level") val grndLevel: Int?,
    val humidity: Int
)

data class WeatherDesc(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(val speed: Double?, val deg: Int?, val gust: Double?)

