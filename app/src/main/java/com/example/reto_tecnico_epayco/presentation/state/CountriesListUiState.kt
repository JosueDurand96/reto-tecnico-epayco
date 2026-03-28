package com.example.reto_tecnico_epayco.presentation.state

import com.example.reto_tecnico_epayco.domain.model.Country

sealed interface CountriesListUiState {
    data object Loading : CountriesListUiState
    data class Success(val countries: List<Country>) : CountriesListUiState
    data class Error(val message: String) : CountriesListUiState
    data object Empty : CountriesListUiState
}
