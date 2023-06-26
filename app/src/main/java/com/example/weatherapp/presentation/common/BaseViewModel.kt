package com.example.weatherapp.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.exception.DomainException
import com.example.weatherapp.domain.usecase.UseCase
import com.example.weatherapp.domain.usecase.UseCaseExecutorProvider

abstract class BaseViewModel(
    useCaseExecutorProvider: UseCaseExecutorProvider
) : ViewModel() {

    private val useCaseExecutor by lazy {
        useCaseExecutorProvider(viewModelScope)
    }

    protected fun <R> execute(
        useCase: UseCase<Unit, R>,
        onSuccess: (R) -> Unit = {},
        onException: (DomainException) -> Unit = {}
    ) {
        execute(useCase, Unit, onSuccess, onException)
    }

    protected fun <P, R> execute(
        useCase: UseCase<P, R>,
        params: P,
        onSuccess: (R) -> Unit = {},
        onException: (DomainException) -> Unit = {}
    ) {
        useCaseExecutor.execute(useCase, params, onSuccess, onException)
    }
}