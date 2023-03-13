package com.example.weather.api

import com.example.weather.datamodel.QueryResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //https://api.openweathermap.org/data/2.5/forecast?q=Haarlem&appid=6b192508e1005c568a5cbb4085f149b9
    @GET("/data/2.5/forecast?units=metric")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ) : QueryResult




    @GET("/data/2.5/find?units=metric")
    suspend fun getQueryResults(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ) : QueryResult
}