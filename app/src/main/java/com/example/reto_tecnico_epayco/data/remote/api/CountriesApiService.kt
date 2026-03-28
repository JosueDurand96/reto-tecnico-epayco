package com.example.reto_tecnico_epayco.data.remote.api

import com.example.reto_tecnico_epayco.data.remote.dto.CountryDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountriesApiService {

    @GET("v3.1/all")
    suspend fun getAllCountries(): List<CountryDto>

    @GET("v3.1/name/{name}")
    suspend fun searchByName(
        @Path("name") name: String,
        @Query("fullText") fullText: Boolean = false
    ): List<CountryDto>

    @GET("v3.1/alpha/{code}")
    suspend fun getByAlphaCode(@Path("code") code: String): List<CountryDto>

    @GET("v3.1/alpha")
    suspend fun getByAlphaCodes(@Query("codes") codes: String): List<CountryDto>
}
