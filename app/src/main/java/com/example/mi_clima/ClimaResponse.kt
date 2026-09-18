package com.example.mi_clima

data class ClimaResponse(
    val latitude: Double,
    val longitude: Double,
    val current: Current
)

data class Current(
    val time: String,
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val apparent_temperature: Double,
    val precipitation: Double,
    val weather_code: Int,
    val wind_speed_10m: Double
)