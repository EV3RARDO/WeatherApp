package com.example.weatherapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

internal object SerializerConfig {
    private val json: Json = Json {
        ignoreUnknownKeys = true
    }

    fun jsonConverterFactory() =
        json.asConverterFactory("application/json".toMediaType())
}
