package com.example.weatherforecastapp.viewmodel


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.WeatherRepository
import com.example.weatherforecastapp.database.ForecastModel
import com.example.weatherforecastapp.model.ForecastItem
import com.example.weatherforecastapp.model.GeoItem
import com.example.weatherforecastapp.model.WeatherResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private var searchJob: Job? = null
    private val _searchResults = MutableStateFlow<List<GeoItem>>(emptyList())
    val searchResults: StateFlow<List<GeoItem>> = _searchResults

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _forecasts = MutableStateFlow<Map<String, WeatherResponse>>(emptyMap())
    val forecasts: StateFlow<Map<String, WeatherResponse>> = _forecasts


    private val _cities = MutableStateFlow<List<ForecastModel>>(emptyList())
    val cities: StateFlow<List<ForecastModel>> = _cities

    fun loadCities() {
        viewModelScope.launch {
            _cities.value = repository.getCities()
        }
    }
    fun search(query: String) {
        viewModelScope.launch {
            val result = repository.searchCities(query)
            result.onSuccess {
                _searchResults.value = it
                _error.value = null
            }.onFailure {
                _error.value = it.message
            }
        }
    }

    fun loadForecast(context: Context, city: String) {
        viewModelScope.launch {
            _loading.value = true

            val result = if (isOnline(context)) {
                repository.getForecastOnline(city)
            } else {
                repository.getForecastOffline(city)
            }

            result.onSuccess {
                _forecasts.value = _forecasts.value + (city to it)
                _error.value = null
            }.onFailure {
                _error.value = it.message
            }

            _loading.value = false
        }
    }

    fun findForecastItem(city: String, dt: Long): ForecastItem? {
        return _forecasts.value[city]?.list?.find { it.dt == dt }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            if (query.isNotBlank()) {
                searchCities(query)
            } else {
                _searchResults.value = emptyList()
            }
        }
    }
    fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                val result = repository.searchCities(query)
                _searchResults.value = result.getOrElse { emptyList() }
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            }
        }
    }
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

}

