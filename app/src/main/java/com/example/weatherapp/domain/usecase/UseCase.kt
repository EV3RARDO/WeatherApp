package com.example.weatherapp.domain.usecase

interface UseCase<P, R> {

    suspend fun execute(params: P, onResult: (R) -> Unit)
}