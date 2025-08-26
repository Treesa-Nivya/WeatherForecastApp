package com.example.weatherforecastapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ForecastModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}