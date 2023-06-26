package com.example.weatherapp.presentation.model

data class Weather(
    val title: String = "",
    val description: String = "",
    val countryName: String = "",
    val countryCode: Int = -1,
    val temp: Float = 0f,
    val feelsLike: Float = 0f,
    val tempMin: Float = 0f,
    val tempMax: Float = 0f,
    val humidity: Int = -1,
    val icon: String = ""
) {

    fun isEmpty() : Boolean = title.isEmpty() && description.isEmpty() && countryName.isEmpty()
}