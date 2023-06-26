package com.example.weatherapp.domain.model

import com.example.weatherapp.presentation.model.Weather

data class WeatherDomain(
    val title: String,
    val description: String,
    val icon: String,
    val countryName: String,
    val countryCode: Int,
    val temp: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
    val humidity: Int
) {
    fun toPresentation() = Weather(
        title = title,
        description = description,
        countryName = countryName,
        countryCode = countryCode,
        temp = temp,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        humidity = humidity
    )
}
