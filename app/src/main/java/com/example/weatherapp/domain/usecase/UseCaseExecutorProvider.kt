package com.example.weatherapp.domain.usecase

import kotlinx.coroutines.CoroutineScope

typealias UseCaseExecutorProvider = @JvmSuppressWildcards (coroutineScope: CoroutineScope) -> UseCaseExecutor