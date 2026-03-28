package com.example.reto_tecnico_epayco.domain.repository

import com.example.reto_tecnico_epayco.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountriesRepository {
    suspend fun getAllCountries(): Result<List<Country>>
    suspend fun searchCountriesByName(query: String): Result<List<Country>>
    suspend fun getCountryByCca3(cca3: String): Result<Country>
    suspend fun getFavoriteCountries(): Result<List<Country>>
    fun observeFavoriteCca3Ids(): Flow<Set<String>>
    suspend fun toggleFavorite(country: Country): Result<Unit>
    suspend fun isFavorite(cca3: String): Boolean
}
