package com.example.reto_tecnico_epayco.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto_tecnico_epayco.domain.usecase.GetFavoriteCountriesUseCase
import com.example.reto_tecnico_epayco.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.reto_tecnico_epayco.presentation.state.CountriesListUiState
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
class FavoritesViewModel @Inject constructor(
    private val getFavoriteCountries: GetFavoriteCountriesUseCase,
    observeFavoriteIds: ObserveFavoriteIdsUseCase
) : ViewModel() {

    private val _listState =
        MutableStateFlow<CountriesListUiState>(CountriesListUiState.Loading)
    val listState: StateFlow<CountriesListUiState> = _listState.asStateFlow()

    val favoriteCca3Ids: StateFlow<Set<String>> = observeFavoriteIds()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    init {
        viewModelScope.launch {
            loadFavorites()
        }
    }

    /**
     * Recarga desde Room sin pasar por [CountriesListUiState.Loading] para evitar parpadeo
     * al cambiar de pestaña o al volver del detalle.
     */
    fun refresh() {
        viewModelScope.launch {
            loadFavorites()
        }
    }

    private suspend fun loadFavorites() {
        _listState.value = getFavoriteCountries().fold(
            onSuccess = { list ->
                when {
                    list.isEmpty() -> CountriesListUiState.Empty
                    else -> CountriesListUiState.Success(list)
                }
            },
            onFailure = { e ->
                CountriesListUiState.Error(e.message ?: "Unknown error")
            }
        )
    }
}
