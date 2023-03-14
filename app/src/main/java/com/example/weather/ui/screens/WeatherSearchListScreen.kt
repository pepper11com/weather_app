package com.example.weather.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.CardColors
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.AlertDialogDefaults.textContentColor
import androidx.compose.material3.AlertDialogDefaults.titleContentColor
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.R
import com.example.weather.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.api.util.Resource
import com.example.weather.datamodel.QueryResult
import com.example.weather.datamodel.Weather


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchWeatherView(
    viewModel: WeatherViewModel = viewModel(),
    navController: NavController
) {

    val searchResults by viewModel.searchResult.observeAsState(initial = Resource.Empty())
    val context = LocalContext.current
    val apiKey = context.getString(R.string.API_KEY)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF242424))
    ) {
        SearchView(
            viewModel = viewModel,
            apiKey = apiKey
        )
        WeatherList(
            searchResults = searchResults,
            navController = navController,
            viewModel = viewModel,
            apiKey = apiKey
        )
    }

}

@Composable
fun WeatherList(
    searchResults: Resource<QueryResult>,
    navController: NavController,
    viewModel: WeatherViewModel,
    apiKey: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        when (searchResults) {
            is Resource.Success -> {
                searchResults.data?.let { weather ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(weather.list) { weather ->
                            WeatherListItem(
                                weather = weather,
                                navController = navController,
                                viewModel = viewModel,
                                apiKey = apiKey
                            )
                        }
                    }
                }
            }
            is Resource.Error -> {
                Text(
                    text = "No results found",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(16.dp)
                )
            }
            is Resource.Empty -> {
                Text(
                    text = stringResource(R.string.search_hint),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherListItem(
    weather: Weather,
    navController: NavController,
    viewModel: WeatherViewModel,
    apiKey: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        colors = CardDefaults.cardColors(
            containerColor  = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
        onClick = {
            viewModel.setSelectedWeather(weather)
            viewModel.getWeatherForecast(weather.name, apiKey)
            navController.navigate(WeatherScreens.DetailScreen.route)
        }
    ) {
        Text(
            text = "${weather.name}, ${weather.sys.country}",
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    viewModel: WeatherViewModel,
    apiKey: String
) {
    val searchQueryState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val customTextSelectionColors = TextSelectionColors(
        handleColor = Color.LightGray,
        backgroundColor = Color.LightGray.copy(alpha = 0.3f)
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        TextField(
            value = searchQueryState.value,
            onValueChange = { value ->
                searchQueryState.value = value

                viewModel.searchWeather(value.text, apiKey)
            },
            modifier = Modifier
                .fillMaxWidth()
//                .background(Color(0xFFFF0000))
                .statusBarsPadding(),
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
            leadingIcon = {
                IconButton(onClick = {
                    //TODO: Your logic here
                    viewModel.searchWeather(searchQueryState.value.text, apiKey)

                    keyboardController?.hide()
                }) {
                    Icon(
                        Icons.Default.Search,
                        tint = Color.White,
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            },
            trailingIcon = {
                if (searchQueryState.value != TextFieldValue("")) {
                    IconButton(
                        onClick = {
                            searchQueryState.value =
                                TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            tint = Color.White,
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_hint),
                    color = Color.White
                )
            },
            singleLine = true,
            shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                cursorColor = Color.White,
                placeholderColor = Color.White,
                containerColor = Color.Transparent,
//                leadingIconColor = Color.White,
//                trailingIconColor = Color.White,
//                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                //copy symbol color
                errorIndicatorColor = Color.Transparent,
            )
        )
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .height(2.dp)
            .background(Color.White),
    )


}