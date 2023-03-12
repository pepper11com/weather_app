package com.example.weather.ui.screens

import androidx.annotation.StringRes
import com.example.weather.R

sealed class WeatherScreens(
    val route: String,
    @StringRes val labelResourceId: Int
) {
    object WeatherScreen : WeatherScreens("weather_screen", R.string.weather_screen)
    object DetailScreen : WeatherScreens("detail_screen", R.string.detail_screen)
}

