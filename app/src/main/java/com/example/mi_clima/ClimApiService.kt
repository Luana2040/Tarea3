package com.example.mi_clima

import retrofit2.http.GET
import retrofit2.http.Query

interface ClimApiService {

    @GET("v1/forecast")
    suspend fun obtenerClima(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
        @Query("timezone") timezone: String
    ): ClimaResponse
}