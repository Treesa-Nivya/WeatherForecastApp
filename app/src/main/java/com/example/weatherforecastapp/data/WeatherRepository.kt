package com.example.weatherforecastapp.data

import com.example.weatherforecastapp.BuildConfig
import com.example.weatherforecastapp.database.ForecastDao
import com.example.weatherforecastapp.database.ForecastModel
import com.example.weatherforecastapp.model.GeoItem
import com.example.weatherforecastapp.model.WeatherResponse
import com.google.gson.Gson

class WeatherRepository(
    private val api: WeatherApiService,
    private val dao: ForecastDao
) {
    private val gson = Gson()
    suspend fun getForecastOnline(city: String): Result<WeatherResponse> = runCatching {
        val response = api.getForecast(city, BuildConfig.API_KEY)
        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Empty response")
            dao.insertForecast(ForecastModel(city, gson.toJson(body)))
            body
        } else {
            throw Exception("Error: ${response.code()} ${response.message()}")
        }
    }

    suspend fun getForecastOffline(city: String): Result<WeatherResponse> = runCatching {
        val local = dao.getForecast(city) ?: throw Exception("No local data")
        gson.fromJson(local.jsonData, WeatherResponse::class.java)
    }

    suspend fun searchCities(query: String): Result<List<GeoItem>> = runCatching {
        val response = api.searchCities(query, 5, BuildConfig.API_KEY)
        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            throw Exception("Error: ${response.code()} ${response.message()}")
        }
    }

    suspend fun getCities(): List<ForecastModel> = dao.getCities()
}
