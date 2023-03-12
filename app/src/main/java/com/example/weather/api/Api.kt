package com.example.weather.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    companion object {
        //https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        //https://openweathermap.org/img/wn/10d@2x.png
        const val BASE_IMAGE_URL = "https://openweathermap.org/img/wn/"

        val client by lazy { createApi() }

        private fun createApi(): ApiService {
            val client = OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                readTimeout(10, TimeUnit.SECONDS)
                writeTimeout(10, TimeUnit.SECONDS)
            }.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}