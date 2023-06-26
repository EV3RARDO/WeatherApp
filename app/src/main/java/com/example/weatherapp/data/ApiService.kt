package com.example.weatherapp.data

import com.example.weatherapp.data.dto.WeatherDTO
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    suspend fun weather(@Query("lat") latitude: Float,
                        @Query("lon") longitud: Float,
                        @Query("appid") appId: String = AppConfig.API_KEY): WeatherDTO

    @GET("data/2.5/weather")
    suspend fun weatherByCity(@Query("q") city: String,
                              @Query("appid") appId: String = AppConfig.API_KEY): WeatherDTO

}


