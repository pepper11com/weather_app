package com.example.weather.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weather.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.weather.api.Api.Companion.BASE_IMAGE_URL
import com.example.weather.api.util.Resource
import java.util.*


@Composable
fun WeatherDetailScreen(
    viewModel: WeatherViewModel = viewModel(),
    navController: NavController
) {
    val selectedWeather = viewModel.selectedWeather
    val weatherForecast by viewModel.weatherForecast.observeAsState(initial = Resource.Empty())
    val selectedTime = remember { mutableStateOf("") }

    selectedWeather?.let { weather ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = weather.name,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                WeatherIcon(icon = weather.weather.first().icon)
                Text(
                    text = "${weather.main.temp.toInt()}°C",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Text(
                text = weather.weather.first().description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Feels like ${weather.main.feelsLike.toInt()}°C",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Humidity ${weather.main.humidity}%",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Wind ${weather.wind.speed} m/s",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            when (weatherForecast) {
                is Resource.Loading -> {
                    Text(
                        text = "Loading forecast...",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                is Resource.Success -> {
                    val forecast = weatherForecast.data
                    forecast?.let {
                        Text(
                            text = "Forecast for the next 5 days:",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Get a list of all available times for the selected day
                        val times = forecast.list.filter {
                            it.dt_txt.startsWith(
                                selectedWeather.dt_txt?.substring(0, 10) ?: ""
                            )
                        }
                            .map { it.dt_txt?.substring(11, 16) ?: "" }
                            .distinct()

                        // Display the weather forecast for the selected time
                        val filteredForecast = forecast.list.filter {
                            it.dt_txt.startsWith(
                                selectedWeather.dt_txt?.substring(0, 10) ?: ""
                            ) && it.dt_txt?.substring(11, 16) == selectedTime.value
                        }


                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            times.forEach { time ->
                                Button(
                                    onClick = {
                                        selectedTime.value = time
                                    },
                                    modifier = Modifier.padding(8.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = if (selectedTime.value == time) Color.Gray else Color.White)
                                ) {
                                    Text(text = time)
                                }
                            }
                        }
                        // Display the weather forecast for the selected time
                        filteredForecast.forEach { forecast ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                WeatherIcon(icon = forecast.weather.first().icon)
                                Text(
                                    text = "${forecast.main.temp.toInt()}°C",
                                    style = MaterialTheme.typography.h2,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = "Error loading forecast",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                else -> {
                    Text(
                        text = "No forecast available",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherIcon(icon: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(
                "$BASE_IMAGE_URL$icon@2x.png"
            )
            .apply(block = fun ImageRequest.Builder.() {
                memoryCachePolicy(CachePolicy.ENABLED)
            }).build()
    )
    Image(
        painter = painter,
        contentDescription = "Weather icon",
        modifier = Modifier.size(64.dp)
    )
}




