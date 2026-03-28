package com.example.reto_tecnico_epayco.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto_tecnico_epayco.domain.usecase.GetAllCountriesUseCase
import com.example.reto_tecnico_epayco.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.reto_tecnico_epayco.domain.usecase.SearchCountriesUseCase
import com.example.reto_tecnico_epayco.presentation.state.CountriesListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_MS = 300L
private const val MIN_SEARCH_LENGTH = 2

@OptIn(FlowPreview::class)
@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getAllCountries: GetAllCountriesUseCase,
    private val searchCountries: SearchCountriesUseCase,
    observeFavoriteIds: ObserveFavoriteIdsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _listState = MutableStateFlow<CountriesListUiState>(CountriesListUiState.Loading)
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
            loadCountries("")
        }
        viewModelScope.launch {
            _searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .drop(1)
                .collectLatest { query ->
                    if (query.trim().length >= MIN_SEARCH_LENGTH) {
                        loadCountries(query)
                    }
                }
        }
    }

    fun onSearchQueryChange(value: String) {
        _searchQuery.update { value }
        if (value.trim().length < MIN_SEARCH_LENGTH) {
            viewModelScope.launch {
                loadCountries(value)
            }
        }
    }

    private suspend fun loadCountries(rawQuery: String) {
        val query = rawQuery.trim()
        _listState.value = CountriesListUiState.Loading

        val result = when {
            query.length < MIN_SEARCH_LENGTH -> getAllCountries()
            else -> searchCountries(query)
        }

        _listState.value = result.fold(
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
