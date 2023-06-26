package com.example.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherapp.data.ApiService
import com.example.weatherapp.data.AppConfig
import com.example.weatherapp.data.SerializerConfig
import com.example.weatherapp.domain.coroutine.IoDispatcher
import com.example.weatherapp.domain.coroutine.MainDispatcher
import com.example.weatherapp.domain.usecase.UseCaseExecutor
import com.example.weatherapp.domain.usecase.UseCaseExecutorProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun providesNycHighSchoolsApi(okHttpClient: OkHttpClient): ApiService =
        Retrofit.Builder()
            .addConverterFactory(SerializerConfig.jsonConverterFactory())
            .baseUrl(AppConfig.baseUrl)
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun providesUseCaseExecutorProvider(): UseCaseExecutorProvider = ::UseCaseExecutor

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("weather_app", Context.MODE_PRIVATE)
    }
}