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

    private val _searchResultResource: MutableLiveData<Resource<QueryResult>> = MutableLiveData(Resource.Empty())

    fun searchWeather(query: String, apiKey: String) {
        _searchResultResource.value = Resource.Loading()

        viewModelScope.launch {
            _searchResultResource.value = weatherRepository.searchWeather(apiKey, query)
        }
    }

    fun setSelectedWeather(weather: Weather) {
        selectedWeather = weather
    }

    fun clearSelectedWeather() {
        selectedWeather = null
    }


}