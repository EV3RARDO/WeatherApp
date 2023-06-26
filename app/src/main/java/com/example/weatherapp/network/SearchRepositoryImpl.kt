package com.example.weatherapp.network

import com.example.weatherapp.data.ApiService
import com.example.weatherapp.domain.SearchRepository
import com.example.weatherapp.domain.model.WeatherDomain
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val apiService: ApiService): SearchRepository {

    override suspend fun weatherByCity(city: String): WeatherDomain {
        return apiService.weatherByCity(city).toDomain()
    }
}