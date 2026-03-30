package com.example.reto_tecnico_epayco.presentation.screens.list

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.reto_tecnico_epayco.R
import com.example.reto_tecnico_epayco.presentation.components.CountriesListSkeleton
import com.example.reto_tecnico_epayco.presentation.components.CountryListItem
import com.example.reto_tecnico_epayco.presentation.components.CountrySearchField
import com.example.reto_tecnico_epayco.presentation.state.CountriesListUiState
import com.example.reto_tecnico_epayco.presentation.viewmodel.CountriesViewModel
import com.example.reto_tecnico_epayco.ui.theme.CountriesTheme
import java.util.Locale

@Composable
fun CountriesScreen(
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CountriesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val notAvailableMsg = stringResource(R.string.voice_search_not_available)
    val voicePrompt = stringResource(R.string.voice_search_prompt)

    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val spoken = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
            ?.trim()
        if (!spoken.isNullOrEmpty()) {
            viewModel.onSearchQueryChange(spoken)
        }
    }

    val listState by viewModel.listState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteCca3Ids.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.screen_countries_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        CountrySearchField(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            onMicrophoneClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toLanguageTag())
                    putExtra(RecognizerIntent.EXTRA_PROMPT, voicePrompt)
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    voiceLauncher.launch(intent)
                } else {
                    Toast.makeText(context, notAvailableMsg, Toast.LENGTH_SHORT).show()
                }
            }
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

            CountriesListUiState.Empty -> EmptyMessage(
                text = stringResource(R.string.empty_countries)
            )

            is CountriesListUiState.Error -> EmptyMessage(
                text = state.message
            )
        }
    }
}

@Composable
private fun EmptyMessage(
    text: String,
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
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, heightDp = 640)
@Composable
private fun CountriesScreenPreview() {
    CountriesTheme {
        CountriesScreen(onCountryClick = {})
    }
}
