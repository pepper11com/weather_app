package com.example.weather.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weather.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.weather.api.Api.Companion.BASE_IMAGE_URL
import com.example.weather.api.util.Resource
import com.example.weather.datamodel.QueryResult
import com.example.weather.datamodel.Weather


@Composable
fun WeatherDetailScreen(
    viewModel: WeatherViewModel = viewModel(),
    navController: NavController
) {
    val selectedWeather = viewModel.selectedWeather
    val weatherForecast by viewModel.weatherForecast.observeAsState(initial = Resource.Empty())
    val selectedTimesByDate = remember { mutableStateMapOf<String, String>() }

    selectedWeather?.let { weather ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top
        ) {

            WeatherDetailsHeader(weather)

            WeatherDetailsItem("Description: ", weather.weather.first().description.replaceFirstChar { it.uppercase() })
            WeatherDetailsItem("Feels like: ", "${weather.main.feelsLike.toInt()}°C")
            WeatherDetailsItem("Humidity: ", "${weather.main.humidity}%")
            WeatherDetailsItem("Wind: ", "${weather.wind.speed} m/s")

            ForecastSection(
                weatherForecast,
                selectedTimesByDate
            )

        }
    }
}

@Composable
fun ForecastSuccess(
    weatherForecast: Resource<QueryResult>,
    selectedTimesByDate: MutableMap<String, String>,
    modifier: Modifier,
){
    val forecast = weatherForecast.data

    forecast?.let {
        Text(
            text = "Forecast",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )



        val forecastByDate = forecast.list.groupBy { it.dt_txt?.substring(0, 10) }
        val first5Dates = forecastByDate.keys.take(5)

        first5Dates.withIndex().forEach { (index, date) ->
            val times = forecastByDate[date]
            if (times != null) {
                Text(
                    text = date ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                )

                val timesForDate = times.map { it.dt_txt?.substring(11, 16) ?: "" }.distinct()

                TimeRow(
                    date,
                    timesForDate,
                    selectedTimesByDate
                )

                WeatherDetails(
                    date,
                    times,
                    selectedTimesByDate
                )

                if (index != first5Dates.size - 1) {
                    Spacer(
                        modifier = Modifier
                            .padding(top = 24.dp, bottom = 24.dp)
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                } else {
                    Spacer(modifier = modifier)
                }
            }
        }
    }
}

@Composable
fun TimeRow(
    date: String?,
    timesForDate: List<String>,
    selectedTimesByDate: MutableMap<String, String>
){
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(timesForDate) { time ->
            Button(
                onClick = {
                    selectedTimesByDate[date ?: ""] = time
                },
                colors = ButtonDefaults.buttonColors(

//                contentColor = if (selectedTimesByDate[date ?: ""] == time) {
//                    Color.LightGray
//                } else {
//                    Color.Gray
//                },
                    containerColor = if (selectedTimesByDate[date ?: ""] == time) {
                        Color.LightGray
                    } else {
                        Color.Gray
                    }
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(text = time)
            }
        }
    }
}

@Composable
fun ForecastSection(
    weatherForecast: Resource<QueryResult>,
    selectedTimesByDate: MutableMap<String, String>
) {
    when (weatherForecast) {
        is Resource.Loading -> {
            WeatherDetailsItem(
                title = "Loading forecast...",
                value = ""
            )
        }
        is Resource.Success -> {
            ForecastSuccess(
                weatherForecast,
                selectedTimesByDate,
                modifier = Modifier.navigationBarsPadding()
            )
        }
        is Resource.Error -> {
            WeatherDetailsItem(
                title = "Error loading forecast: ",
                value = weatherForecast.message ?: ""
            )
        }
        else -> {
            WeatherDetailsItem(
                title = "No forecast available",
                value = ""
            )
        }
    }
}

@Composable
fun WeatherDetails(
    date: String?,
    times: List<Weather>,
    selectedTimesByDate: MutableMap<String, String>
){
    val selectedTime = selectedTimesByDate[date ?: ""]
    selectedTime?.let { time ->

        val selectedWeather = times.firstOrNull { it.dt_txt?.substring(11, 16) == time }
        selectedWeather?.let { weather ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                WeatherIcon(icon = weather.weather.first().icon)

                WeatherDetailsItem(
                    title = "Temperature: ",
                    value = weather.main.temp.toInt().toString() + "°C",
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            WeatherDetailsItem(
                title = "Description: ",
                value =  weather.weather.first().description.replaceFirstChar { it.uppercase() }
            )
            WeatherDetailsItem(
                title = "Feels like: ",
                value = "${weather.main.feelsLike.toInt()}°C"
            )
            WeatherDetailsItem(
                title = "Humidity: ",
                value = "${weather.main.humidity}%"
            )
            WeatherDetailsItem(
                title = "Wind: ",
                value = "${weather.wind.speed} m/s"
            )
        }
    }
}

@Composable
fun WeatherDetailsItem(title: String, value: String, modifier: Modifier = Modifier.padding(bottom = 8.dp)) {
    Text(
        text = "$title $value",
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

@Composable
fun WeatherDetailsHeader(weather: Weather) {
    Text(
        text = weather.name,
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier
            .statusBarsPadding()
            .padding(bottom = 8.dp)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        WeatherIcon(icon = weather.weather.first().icon)
        Text(
            text = "${weather.main.temp.toInt()}°C",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
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




