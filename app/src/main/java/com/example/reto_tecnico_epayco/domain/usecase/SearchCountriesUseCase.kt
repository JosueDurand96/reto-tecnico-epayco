package com.example.reto_tecnico_epayco.domain.usecase

import com.example.reto_tecnico_epayco.domain.model.Country
import com.example.reto_tecnico_epayco.domain.repository.CountriesRepository
import javax.inject.Inject

class SearchCountriesUseCase @Inject constructor(
    private val repository: CountriesRepository
) {
    suspend operator fun invoke(query: String): Result<List<Country>> =
        repository.searchCountriesByName(query)
}
