package com.example.reto_tecnico_epayco.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto_tecnico_epayco.domain.usecase.GetCountryByCodeUseCase
import com.example.reto_tecnico_epayco.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.reto_tecnico_epayco.domain.usecase.ToggleFavoriteUseCase
import com.example.reto_tecnico_epayco.presentation.navigation.NavArgs
import com.example.reto_tecnico_epayco.presentation.state.CountryDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCountryByCode: GetCountryByCodeUseCase,
    observeFavoriteIds: ObserveFavoriteIdsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    private val cca3: String = checkNotNull(savedStateHandle.get<String>(NavArgs.CCA3))

    private val _uiState =
        MutableStateFlow<CountryDetailUiState>(CountryDetailUiState.Loading)
    val uiState: StateFlow<CountryDetailUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = observeFavoriteIds()
        .map { ids -> ids.contains(cca3) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = CountryDetailUiState.Loading
            _uiState.value = getCountryByCode(cca3).fold(
                onSuccess = { CountryDetailUiState.Success(it) },
                onFailure = { e ->
                    CountryDetailUiState.Error(e.message ?: "Unknown error")
                }
            )
        }
    }

    fun onToggleFavorite() {
        viewModelScope.launch {
            val country = (uiState.value as? CountryDetailUiState.Success)?.country
                ?: return@launch
            toggleFavorite(country)
        }
    }
}
