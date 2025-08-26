package com.example.weatherforecastapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: ForecastModel)

    @Query("SELECT * FROM forecast_table WHERE LOWER(city) = LOWER(:city) LIMIT 1")
    suspend fun getForecast(city: String): ForecastModel?

    @Query("SELECT * FROM forecast_table")
    suspend fun getCities(): List<ForecastModel>

}