package com.example.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.api.util.Resource
import com.example.weather.datamodel.QueryResult
import com.example.weather.datamodel.Weather
import com.example.weather.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRepository = WeatherRepository()

    var selectedWeather: Weather? = null
        private set

    val searchResult: MutableLiveData<Resource<QueryResult>>
        get() = _searchResultResource

    val weatherForecast: MutableLiveData<Resource<QueryResult>>
        get() = _weatherForecastResource

    private val _searchResultResource: MutableLiveData<Resource<QueryResult>> = MutableLiveData(Resource.Empty())
    private val _weatherForecastResource: MutableLiveData<Resource<QueryResult>> = MutableLiveData(Resource.Empty())

    fun searchWeather(query: String, apiKey: String) {
        _searchResultResource.value = Resource.Loading()

        viewModelScope.launch {
            _searchResultResource.value = weatherRepository.searchWeather(apiKey, query)
        }
    }

    //https://api.openweathermap.org/data/2.5/forecast?q=Haarlem&appid=6b192508e1005c568a5cbb4085f149b9
    fun getWeatherForecast(query: String, apiKey: String) {
        _weatherForecastResource.value = Resource.Loading()

        viewModelScope.launch {
            _weatherForecastResource.value = weatherRepository.getWeatherForecast(apiKey, query)
        }
    }

    fun setSelectedWeather(weather: Weather) {
        selectedWeather = weather
    }

    fun setSelectedWeatherForecast(weather: Weather) {
        selectedWeather = weather
    }

    fun clearSelectedWeather() {
        selectedWeather = null
    }


}