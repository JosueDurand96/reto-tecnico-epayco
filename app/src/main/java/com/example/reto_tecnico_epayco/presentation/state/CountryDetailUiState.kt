package com.example.reto_tecnico_epayco.presentation.state

import com.example.reto_tecnico_epayco.domain.model.Country

sealed interface CountryDetailUiState {
    data object Loading : CountryDetailUiState
    data class Success(val country: Country) : CountryDetailUiState
    data class Error(val message: String) : CountryDetailUiState
}
