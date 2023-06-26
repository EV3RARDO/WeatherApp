package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.exception.DomainException
import com.example.weatherapp.domain.exception.UnknownException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UseCaseExecutor(private val coroutineScope: CoroutineScope) {

    fun <P, R> execute(
        useCase: UseCase<P, R>,
        params: P,
        onResult: (R) -> Unit = {},
        onException: (DomainException) -> Unit = {}
    ) {
        coroutineScope.launch {
            try {
                useCase.execute(params, onResult)
            } catch (ignore: CancellationException) {
            } catch (throwable: Throwable) {
                onException( (throwable as? DomainException) ?: UnknownException(throwable))
            }
        }
    }
}