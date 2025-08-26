package com.example.weatherforecastapp.uiscreen


import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.weatherforecastapp.Screens
import com.example.weatherforecastapp.model.ForecastItem
import com.example.weatherforecastapp.viewmodel.WeatherViewModel

@Composable
fun ForecastData(
    navController: NavController,
    city: String,
    viewModel: WeatherViewModel,
    context: Context
) {
    val forecastsMap by viewModel.forecasts.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(city) {
        if (forecastsMap[city] == null) {
            viewModel.loadForecast(context, city)
        }
    }

    val response = forecastsMap[city]

    Column(Modifier.fillMaxSize()) {
        Text(
            text = city,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(24.dp)
        )

        when {
            loading && response == null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            response != null -> {
                response.list?.let { forecastList ->
                    LazyColumn {
                        items(forecastList) { item ->
                            ForecastCard(item) {
                                navController.navigate(
                                    Screens.ForecastDetailedData.route(city, item.dt)
                                )
                            }
                        }
                    }
                } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No forecast data available")
                }
            }
            else -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No data available. Connect to internet")
                }
            }
        }
    }
}

@Composable
private fun ForecastCard(item: ForecastItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${item.weather.firstOrNull()?.icon ?: "01d"}@2x.png"),
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text("${item.main.temp}°C", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(4.dp))
                Text(item.weather.firstOrNull()?.description ?: "", modifier = Modifier.padding(4.dp))
                Text(item.dtTxt, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(4.dp))
            }
        }
    }
}
