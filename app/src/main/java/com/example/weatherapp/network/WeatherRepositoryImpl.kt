package com.example.weatherapp.network

import com.example.weatherapp.data.ApiService
import com.example.weatherapp.domain.model.WeatherDomain
import com.example.weatherapp.domain.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val apiService: ApiService): WeatherRepository {

    // TODO Handle error cases for api calls

    override suspend fun weatherByCoordinates(lat: Float, lon: Float): WeatherDomain {
        return apiService.weather(lat, lon).toDomain()
    }

    override suspend fun weatherByCity(city: String): WeatherDomain {
        return apiService.weatherByCity(city).toDomain()
    }
}