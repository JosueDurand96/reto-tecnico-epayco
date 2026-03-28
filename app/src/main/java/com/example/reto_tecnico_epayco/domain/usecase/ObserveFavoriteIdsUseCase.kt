package com.example.reto_tecnico_epayco.domain.usecase

import com.example.reto_tecnico_epayco.domain.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteIdsUseCase @Inject constructor(
    private val repository: CountriesRepository
) {
    operator fun invoke(): Flow<Set<String>> = repository.observeFavoriteCca3Ids()
}
