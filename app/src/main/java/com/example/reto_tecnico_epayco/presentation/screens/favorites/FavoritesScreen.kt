package com.example.reto_tecnico_epayco.presentation.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.reto_tecnico_epayco.R
import com.example.reto_tecnico_epayco.presentation.components.CountriesListSkeleton
import com.example.reto_tecnico_epayco.presentation.components.CountryListItem
import com.example.reto_tecnico_epayco.presentation.state.CountriesListUiState
import com.example.reto_tecnico_epayco.presentation.viewmodel.FavoritesViewModel
import com.example.reto_tecnico_epayco.ui.theme.CountriesTheme

@Composable
fun FavoritesScreen(
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteCca3Ids.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Solo al volver del detalle (u otra pantalla): sincronizar sin mostrar skeleton otra vez.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.screen_favorites_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.screen_favorites_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (val state = listState) {
            CountriesListUiState.Loading -> CountriesListSkeleton()
            is CountriesListUiState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(state.countries, key = { it.cca3 }) { country ->
                        CountryListItem(
                            country = country,
                            isFavorite = favoriteIds.contains(country.cca3),
                            onClick = { onCountryClick(country.cca3) }
                        )
                    }
                }
            }

            CountriesListUiState.Empty -> EmptyFavoritesMessage()

            is CountriesListUiState.Error -> EmptyFavoritesMessage(text = state.message)
        }
    }
}

@Composable
private fun EmptyFavoritesMessage(
    text: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text ?: stringResource(R.string.empty_favorites),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, heightDp = 640)
@Composable
private fun FavoritesScreenPreview() {
    CountriesTheme {
        FavoritesScreen(onCountryClick = {})
    }
}
