package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.ui.screens.SearchWeatherView
import com.example.weather.ui.screens.WeatherDetailScreen
import com.example.weather.ui.screens.WeatherScreens
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isDarkTheme = resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES

        setContent {
            WeatherTheme(
                darkTheme = isDarkTheme
            ) {
                WindowCompat.setDecorFitsSystemWindows(window, false)

                window.statusBarColor = Color.Transparent.toArgb()

                WeatherApp()
            }
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WeatherApp() {
    val navController = rememberAnimatedNavController()

    Scaffold(

    ) { innerPadding ->
        NavHostScreen(navController, innerPadding)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun NavHostScreen(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: WeatherViewModel = viewModel()
) {
    AnimatedNavHost(
        navController,
        startDestination = WeatherScreens.WeatherScreen.route,
        Modifier.padding(innerPadding)
    ) {
        composable(
            route = WeatherScreens.WeatherScreen.route,
            exitTransition = { ->
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),

                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {  ->
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),

                    ) + fadeIn(animationSpec = tween(300))
            }

        ) {
            SearchWeatherView(
                viewModel = viewModel,
                navController = navController,
            )
        }
        composable(
            route = WeatherScreens.DetailScreen.route,

            enterTransition = { ->
                slideInHorizontally(
                    initialOffsetX = { 300 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),

                    ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {  ->
                slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),

                    ) + fadeOut(animationSpec = tween(300))
            }

        ) {
            WeatherDetailScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}