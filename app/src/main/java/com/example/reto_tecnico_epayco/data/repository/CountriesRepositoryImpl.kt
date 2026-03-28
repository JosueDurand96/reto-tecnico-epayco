package com.example.reto_tecnico_epayco.data.repository

import com.example.reto_tecnico_epayco.data.local.dao.FavoriteDao
import com.example.reto_tecnico_epayco.data.mapper.CountryMapper
import com.example.reto_tecnico_epayco.data.mapper.FavoriteCountryEntityMapper
import com.example.reto_tecnico_epayco.data.remote.api.CountriesApiService
import com.example.reto_tecnico_epayco.domain.model.Country
import com.example.reto_tecnico_epayco.domain.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
    private val api: CountriesApiService,
    private val favoriteDao: FavoriteDao,
    private val mapper: CountryMapper,
    private val favoriteEntityMapper: FavoriteCountryEntityMapper
) : CountriesRepository {

    override suspend fun getAllCountries(): Result<List<Country>> =
        runCatching {
            api.getAllCountries().map { mapper.toDomain(it) }
        }.mapNetworkErrors()

    override suspend fun searchCountriesByName(query: String): Result<List<Country>> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return Result.success(emptyList())
        return runCatching {
            api.searchByName(trimmed, fullText = false).map { mapper.toDomain(it) }
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { e ->
                if (e is HttpException && e.code() == 404) {
                    Result.success(emptyList())
                } else {
                    Result.failure(e)
                }
            }
        )
    }

    override suspend fun getCountryByCca3(cca3: String): Result<Country> =
        runCatching {
            favoriteDao.getByCca3(cca3)?.let { favoriteEntityMapper.toDomain(it) }
                ?: run {
                    val list = api.getByAlphaCode(cca3)
                    val dto = list.firstOrNull()
                        ?: error("Country not found")
                    mapper.toDomain(dto)
                }
        }.mapNetworkErrors()

    override suspend fun getFavoriteCountries(): Result<List<Country>> =
        runCatching {
            favoriteDao.getFavoritesOrdered().map { favoriteEntityMapper.toDomain(it) }
        }

    override fun observeFavoriteCca3Ids(): Flow<Set<String>> =
        favoriteDao.observeFavoriteCca3Ordered().map { it.toSet() }

    override suspend fun toggleFavorite(country: Country): Result<Unit> =
        runCatching {
            if (favoriteDao.isFavorite(country.cca3)) {
                favoriteDao.deleteByCca3(country.cca3)
            } else {
                favoriteDao.insert(favoriteEntityMapper.toEntity(country))
            }
        }

    override suspend fun isFavorite(cca3: String): Boolean =
        favoriteDao.isFavorite(cca3)
}

private fun <T> Result<T>.mapNetworkErrors(): Result<T> =
    fold(
        onSuccess = { Result.success(it) },
        onFailure = { e ->
            when (e) {
                is IOException -> Result.failure(NetworkException("No hay conexión. Comprueba tu red.", e))
                is HttpException -> Result.failure(
                    NetworkException("Error del servidor (${e.code()}). Inténtalo de nuevo.", e)
                )
                else -> Result.failure(e)
            }
        }
    )

class NetworkException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
