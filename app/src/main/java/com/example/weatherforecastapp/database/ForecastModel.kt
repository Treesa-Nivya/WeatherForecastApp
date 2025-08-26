package com.example.weatherforecastapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_table")
data class ForecastModel(
    @PrimaryKey val city: String,
    val jsonData: String,
    val timestamp: Long = System.currentTimeMillis()
)