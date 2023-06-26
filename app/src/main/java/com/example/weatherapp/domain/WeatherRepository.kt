package com.example.weatherapp.domain

import com.example.weatherapp.domain.model.WeatherDomain

interface WeatherRepository {

    suspend fun weatherByCoordinates(lat: Float, lon: Float): WeatherDomain

    suspend fun weatherByCity(city: String): WeatherDomain
}