package com.example.weatherforecastapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.weatherforecastapp.data.RetrofitInstance
import com.example.weatherforecastapp.data.WeatherRepository
import com.example.weatherforecastapp.database.AppDatabase
import com.example.weatherforecastapp.uiscreen.ForecastData
import com.example.weatherforecastapp.uiscreen.ForecastDetailedData
import com.example.weatherforecastapp.uiscreen.LocationSelection
import com.example.weatherforecastapp.viewmodel.WeatherViewModel
import com.example.weatherforecastapp.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "weather-db"
        ).build()

        setContent {
            val navController = rememberNavController()

            val vm: WeatherViewModel = viewModel(
                factory = WeatherViewModelFactory(
                    WeatherRepository(RetrofitInstance.api, db.forecastDao())
                )
            )

            NavHost(
                navController = navController,
                startDestination = Screens.LocationSelection.route
            ) {
                composable(Screens.LocationSelection.route) {
                    LocationSelection(navController, vm)
                }
                composable(
                    route = Screens.ForecastData.route,
                    arguments = listOf(navArgument("city") { type = NavType.StringType })
                ) {
                    val context = LocalContext.current
                    ForecastData(navController, it.arguments?.getString("city") ?: "", vm,context)
                }
                composable(
                    route = Screens.ForecastDetailedData.route,
                    arguments = listOf(
                        navArgument("city") { type = NavType.StringType },
                        navArgument("dt") { type = NavType.LongType }
                    )
                ) {
                    ForecastDetailedData(
                        it.arguments?.getString("city") ?: "",
                        it.arguments?.getLong("dt") ?: 0L,
                        vm
                    )
                }
            }
        }
    }
}

