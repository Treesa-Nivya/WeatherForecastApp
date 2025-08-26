package com.example.weatherforecastapp.uiscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.weatherforecastapp.viewmodel.WeatherViewModel

@Composable
fun ForecastDetailedData(
    city: String,
    dt: Long,
    viewModel: WeatherViewModel
) {
    val item = viewModel.findForecastItem(city, dt)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Weather",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF4FC3F7),
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(12.dp))
        Image(
            painter = rememberAsyncImagePainter(
                "https://openweathermap.org/img/wn/${item?.weather?.firstOrNull()?.icon ?: "01d"}@4x.png"
            ),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(Modifier.height(8.dp))

        if (item != null) {
            Text(
                text = "${item?.main?.temp ?: "--"}°C",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(text = item?.dtTxt ?: "")

            Spacer(Modifier.height(16.dp))

            DetailRow("Humidity", "${item?.main?.humidity ?: "--"}%")
            DetailRow("Wind", "${item?.wind?.speed ?: "--"} m/s")
            DetailRow("Min Temp", "${item?.main?.tempMin ?: "--"}°C")
            DetailRow("Max Temp", "${item?.main?.tempMax ?: "--"}°C")
        }
        else {

            Text("No detailed data available offline. Please connect to internet.")
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
    Divider(thickness = 0.8.dp, color = Color(0xFFE0E0E0))
}
