package com.example.weatherapp.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
abstract  class BackgroundExecutingUseCase<P, R>(private val ioDispatcher: CoroutineDispatcher): UseCase<P, R> {

    final override suspend fun execute(params: P, onResult: (R) -> Unit) {
        val result = withContext(ioDispatcher) {
            executeInBackground(params)
        }
        onResult(result)

    }

    abstract suspend fun executeInBackground(params: P):  R
}