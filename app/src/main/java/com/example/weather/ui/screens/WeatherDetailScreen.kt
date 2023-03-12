package com.example.weather.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


@Composable
fun WeatherDetailScreen(
    viewModel: WeatherViewModel = viewModel(),
    navController: NavController
) {
    val selectedWeather = viewModel.selectedWeather

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
                text = weather.weather.first().description,
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
        }
    }
}

////https://openweathermap.org/img/wn/10d@2x.png
// const val BASE_IMAGE_URL = "https://openweathermap.org/img/wn/"
//val backdropPath = rememberAsyncImagePainter(
//                ImageRequest.Builder(LocalContext.current)
//                    .data(
//                        data =
//                    )
//                    .apply(block = fun ImageRequest.Builder.() {
//                        memoryCachePolicy(CachePolicy.ENABLED)
//                    }).build()
//            )


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

