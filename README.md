Weather Forecast App

A mobile app to search and view weather forecasts for cities, supporting online (API) and offline (local cache) modes.

Features

Search for cities and view weather forecasts.
Saved locations for quick access.
Detailed forecast: temperature, humidity, wind, min/max temps, and weather icons.
Works offline using cached data.


Technologies

Kotlin, Android Studio, 
Jetpack Compose, 
Room Database, 
Retrofit & Gson, 
StateFlow for UI, 
OpenWeatherMap API, 


How It Works

User opens the app and sees a list of saved cities.
User can search for a city using the search bar.
If internet is available, the app fetches live data from API.
If offline, the app shows cached data for saved cities or displays a message for new searches.
Clicking on a city navigates to a detailed forecast screen showing weather info for selected date.

