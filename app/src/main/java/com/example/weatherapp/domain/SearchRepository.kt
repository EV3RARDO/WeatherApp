package com.example.weatherapp.domain

import com.example.weatherapp.domain.model.WeatherDomain

interface SearchRepository {

    suspend fun weatherByCity(city: String): WeatherDomain

}