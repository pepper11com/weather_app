package com.example.weather.repository

import android.util.Log
import com.example.weather.api.Api
import com.example.weather.api.ApiService
import com.example.weather.api.util.Resource
import com.example.weather.datamodel.QueryResult
import kotlinx.coroutines.withTimeout

class WeatherRepository {
    private val weatherApi: ApiService = Api.client

    suspend fun searchWeather(apiKey: String, query: String): Resource<QueryResult> {
        val response = try {
            withTimeout(5_000) {
                weatherApi.getQueryResults(query, apiKey)
            }
        } catch(e: Exception) {
            Log.e("MovieRepository", e.message ?: "No exception message available")
            return Resource.Error("An unknown error occurred")
        }

        return Resource.Success(response)
    }

    //https://api.openweathermap.org/data/2.5/forecast?q=Haarlem&appid=6b192508e1005c568a5cbb4085f149b9
    suspend fun getWeatherForecast(apiKey: String, query: String): Resource<QueryResult> {
        val response = try {
            withTimeout(5_000) {
                weatherApi.getWeather(query, apiKey)
            }
        } catch(e: Exception) {
            Log.e("MovieRepository", e.message ?: "No exception message available")
            return Resource.Error("An unknown error occurred")
        }

        return Resource.Success(response)
    }

}