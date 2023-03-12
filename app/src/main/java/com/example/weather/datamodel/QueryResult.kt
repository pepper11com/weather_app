package com.example.weather.datamodel

data class QueryResult(
    //{"message":"accurate","cod":"200","count":2,"list":[{"id":2755002,"name":"Gemeente Haarlem","coord":{"lat":52.3667,"lon":4.6333},"main":{"temp":282.67,"feels_like":278.96,"temp_min":281.44,"temp_max":283.14,"pressure":1003,"humidity":85},"dt":1678651589,"wind":{"speed":8.94,"deg":230},"sys":{"country":"NL"},"rain":null,"snow":null,"clouds":{"all":75},"weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}]},{"id":2755003,"name":"Haarlem","coord":{"lat":52.3808,"lon":4.6368},"main":{"temp":282.64,"feels_like":278.97,"temp_min":281.43,"temp_max":283.13,"pressure":1002,"humidity":85},"dt":1678651757,"wind":{"speed":8.75,"deg":230},"sys":{"country":"NL"},"rain":null,"snow":null,"clouds":{"all":75},"weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}]}]}
    val message: String,
    val cod: String,
    val count: Int,
    val list: List<Weather>
)