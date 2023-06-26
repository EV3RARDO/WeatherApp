package com.example.weatherapp.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.AnyThread
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.domain.SearchRepository
import com.example.weatherapp.domain.coroutine.IoDispatcher
import com.example.weatherapp.domain.exception.DomainException
import com.example.weatherapp.domain.usecase.GetWeatherByCityUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByCoordinatesUseCase
import com.example.weatherapp.domain.usecase.UseCaseExecutorProvider
import com.example.weatherapp.presentation.common.BaseViewModel
import com.example.weatherapp.presentation.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val CITY: String = "city" // TODO create SharedPreferences Wrapper and move variables there.

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getWeatherByCoordinatesUseCase: GetWeatherByCoordinatesUseCase,
    private val searchRepository: SearchRepository,
    private val sharedPreferences: SharedPreferences,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    useCaseExecutorProvider: UseCaseExecutorProvider
) : BaseViewModel(useCaseExecutorProvider) {

    private val _stateView  = MutableLiveData(Weather()) // TODO use ViewState instead of `Weather` model
    val stateView: MutableLiveData<Weather> get() = _stateView

    private val _searchQuery = MutableStateFlow("")

    // Auto-load the last city searched upon app launch.
    init {
        val lastCitySearched = sharedPreferences.getString(CITY, "")
        if (!lastCitySearched.isNullOrEmpty()) {
            searchByCity(lastCitySearched)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        saveCity(query)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResult: Flow<Weather> = _searchQuery.debounce(1000).mapLatest {
        if (it.isEmpty()) {
            return@mapLatest Weather()
        } else {
            return@mapLatest searchWeatherByCity(it)
        }
    }

    /**
     * My preference is to use Interactor/UseCase to trigger api calls, but I had to
     * make an exception here to avoid making multiple api calls while the user is typing.
     */
    @AnyThread
    private suspend fun searchWeatherByCity(city: String): Weather =
        withContext(ioDispatcher) {
            searchRepository.weatherByCity(city).toPresentation()
        }

    private fun searchByCity(city: String) {
        execute(
            useCase = getWeatherByCityUseCase,
            params = GetWeatherByCityUseCase.Params(city),
            onSuccess = ::onSuccess,
            onException = ::onException
        )
    }

    fun searchByCoordinates(lat: Float, lon: Float) {
        execute(
            useCase = getWeatherByCoordinatesUseCase,
            params = GetWeatherByCoordinatesUseCase.Params(lat, lon),
            onSuccess = ::onSuccess,
            onException = ::onException
        )
    }

    private fun onSuccess(weather: Weather) {
        _stateView.value = weather
        saveCity(weather.countryName)
    }

    private fun saveCity(city: String) {
        sharedPreferences.edit().putString(CITY, city).apply()
    }

    private fun onException(e: DomainException) { // Handle Error Case
        Log.d("MainViewModel", "$e")
    }

}


