package com.example.weatherapp.di

import com.example.weatherapp.domain.SearchRepository
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.network.SearchRepositoryImpl
import com.example.weatherapp.network.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindings {

    @Binds
    abstract fun  providesWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    abstract fun providesSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}