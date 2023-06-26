package com.example.weatherapp.data.dto

import com.example.weatherapp.domain.model.WeatherDomain
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDTO(
    val coord: coord,
    val weather: List<WeatherItem>,
    val name: String,
    val cod: Int,
    val main: Main
) {
    fun toDomain(): WeatherDomain = WeatherDomain(
        title = weather.getOrNull(0)?.main ?: "",
        description = weather.getOrNull(0)?.description ?: "",
        icon = weather.getOrNull(0)?.icon ?: "",
        countryName = name,
        countryCode = cod,
        temp = main.temp,
        feelsLike = main.feels_like,
        tempMin = main.temp_min,
        tempMax = main.temp_max,
        humidity = main.humidity
    )
}

@Serializable
data class coord(val lon: Float, val lat: Float)

@Serializable
data class WeatherItem(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

/**
 * main: {
temp: 300.24,
feels_like: 300.98,
temp_min: 298.12,
temp_max: 301.51,
pressure: 1019,
humidity: 55
},
 */
@Serializable
data class Main(
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val humidity: Int
)
