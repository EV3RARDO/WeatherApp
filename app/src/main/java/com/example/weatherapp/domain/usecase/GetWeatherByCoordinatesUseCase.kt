package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.coroutine.IoDispatcher
import com.example.weatherapp.presentation.model.Weather
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetWeatherByCoordinatesUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BackgroundExecutingUseCase<GetWeatherByCoordinatesUseCase.Params, Weather>(ioDispatcher) {

    override suspend fun executeInBackground(params: Params): Weather {
        return weatherRepository.weatherByCoordinates(
            lat = params.lat,
            lon = params.lon
        ).toPresentation()
    }

    data class Params(
        val lat: Float,
        val lon: Float
    )
}