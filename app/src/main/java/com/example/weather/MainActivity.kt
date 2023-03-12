package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.ui.screens.SearchWeatherView
import com.example.weather.ui.screens.WeatherDetailScreen
import com.example.weather.ui.screens.WeatherScreens

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                WeatherApp()
            }
        }
    }
}
@Composable
fun WeatherApp() {
    val navController = rememberNavController()

    Scaffold(

    ) { innerPadding ->
        NavHostScreen(navController, innerPadding)
    }
}

@Composable
private fun NavHostScreen(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: WeatherViewModel = viewModel()
) {
    NavHost(
        navController,
        startDestination = WeatherScreens.WeatherScreen.route,
        Modifier.padding(innerPadding)
    ) {
        composable( WeatherScreens.WeatherScreen.route) {
            SearchWeatherView(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable( WeatherScreens.DetailScreen.route) {
            WeatherDetailScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}