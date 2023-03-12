package com.example.weather.api

import com.example.weather.datamodel.QueryResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    //For temperature in Celsius use units=metric

    @GET("/data/2.5/find?units=metric")
    suspend fun getQueryResults(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ):QueryResult
}