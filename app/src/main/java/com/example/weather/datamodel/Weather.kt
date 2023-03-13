package com.example.weather.datamodel

import com.google.gson.annotations.SerializedName

data class Weather(
    //{"message":"accurate","cod":"200","count":2,"list":[{"id":2755002,"name":"Gemeente Haarlem","coord":{"lat":52.3667,"lon":4.6333},"main":{"temp":282.67,"feels_like":278.96,"temp_min":281.44,"temp_max":283.14,"pressure":1003,"humidity":85},"dt":1678651589,"wind":{"speed":8.94,"deg":230},"sys":{"country":"NL"},"rain":null,"snow":null,"clouds":{"all":75},"weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}]},{"id":2755003,"name":"Haarlem","coord":{"lat":52.3808,"lon":4.6368},"main":{"temp":282.64,"feels_like":278.97,"temp_min":281.43,"temp_max":283.13,"pressure":1002,"humidity":85},"dt":1678651757,"wind":{"speed":8.75,"deg":230},"sys":{"country":"NL"},"rain":null,"snow":null,"clouds":{"all":75},"weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}]}]}
    val id: Int,
    val name: String,
    val coord: Coord,
    val main: Main,
    val dt: Int,
    val wind: Wind,
    val sys: Sys,
    val rain: Rain,
    val snow: Snow,
    val clouds: Clouds,
    val weather: List<WeatherDescription>,
    val dt_txt: String
) {


    data class Coord(
        val lat: Double,
        val lon: Double
    )

    data class Main(
        val temp: Double,
        @SerializedName("feels_like") val feelsLike: Double,
        @SerializedName("temp_min") val tempMin: Double,
        @SerializedName("temp_max") val tempMax: Double,
        val pressure: Int,
        val humidity: Int
    )

    data class Wind(
        val speed: Double,
        val deg: Int
    )

    data class Sys(
        val country: String
    )

    data class Rain(
        val rain: Double
    )

    data class Snow(
        val snow: Double
    )

    data class Clouds(
        val all: Int
    )

    data class WeatherDescription(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )
}


