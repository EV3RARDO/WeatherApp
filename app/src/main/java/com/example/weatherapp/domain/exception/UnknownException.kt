package com.example.weatherapp.domain.exception


class UnknownException(throwable: Throwable): DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}