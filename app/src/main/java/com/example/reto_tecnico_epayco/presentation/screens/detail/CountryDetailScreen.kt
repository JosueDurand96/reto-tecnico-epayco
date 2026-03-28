package com.example.reto_tecnico_epayco.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Surface
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.reto_tecnico_epayco.R
import com.example.reto_tecnico_epayco.domain.model.Country
import com.example.reto_tecnico_epayco.domain.model.CurrencyInfo
import com.example.reto_tecnico_epayco.presentation.state.CountryDetailUiState
import com.example.reto_tecnico_epayco.presentation.viewmodel.CountryDetailViewModel
import com.example.reto_tecnico_epayco.ui.theme.CountriesTheme
import com.example.reto_tecnico_epayco.ui.theme.OutlineSoft
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CountryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.detail_back_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = stringResource(R.string.cd_toggle_favorite),
                            tint = if (isFavorite) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            CountryDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CountryDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is CountryDetailUiState.Success -> {
                CountryDetailContent(
                    country = state.country,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CountryDetailContent(
    country: Country,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState()
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

    Column(
        modifier = modifier
            .verticalScroll(scroll)
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            DetailFlag(
                url = country.flagUrl,
                name = country.commonName,
                modifier = Modifier.size(width = 120.dp, height = 80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = country.commonName,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = MaterialTheme.typography.displayLarge.fontSize * 0.65f),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = country.officialName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            DetailColumn(
                country = country,
                numberFormat = numberFormat,
                modifier = Modifier.weight(1f)
            )
            DetailColumnRight(
                country = country,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DetailColumn(
    country: Country,
    numberFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        DetailLabel(stringResource(R.string.label_coat_of_arms))
        CoatOfArmsBlock(url = country.coatOfArmsUrl, name = country.commonName)
        DetailField(stringResource(R.string.label_region), country.region)
        country.subregion?.let { DetailField(stringResource(R.string.label_subregion), it) }
        DetailField(
            stringResource(R.string.label_capital),
            country.capital.ifEmpty { stringResource(R.string.no_capital) }
        )
        country.area?.let {
            DetailField(stringResource(R.string.label_area), numberFormat.format(it))
        }
    }
}

@Composable
private fun DetailColumnRight(
    country: Country,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        DetailField(stringResource(R.string.label_population), numberFormat.format(country.population))
        DetailField(
            stringResource(R.string.label_languages),
            country.languages.joinToString().ifEmpty { stringResource(R.string.value_unknown) }
        )
        CarSideRow(side = country.carSide)
        DetailField(
            stringResource(R.string.label_currencies),
            formatCurrencies(country.currencies)
        )
        DetailField(
            stringResource(R.string.label_timezones),
            country.timezones.joinToString().ifEmpty { stringResource(R.string.value_unknown) }
        )
    }
}

@Composable
private fun CoatOfArmsBlock(
    url: String?,
    name: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, OutlineSoft.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.cd_coat_of_arms, name),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun DetailFlag(
    url: String?,
    name: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.cd_flag, name),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, OutlineSoft.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun DetailLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun DetailField(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun CarSideRow(side: String) {
    Column {
        Text(
            text = stringResource(R.string.label_car_side),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val normalized = side.lowercase(Locale.getDefault())
            CarSideChip(
                label = stringResource(R.string.car_side_left),
                selected = normalized == "left"
            )
            CarSideChip(
                label = stringResource(R.string.car_side_right),
                selected = normalized == "right"
            )
        }
    }
}

@Composable
private fun CarSideChip(
    label: String,
    selected: Boolean
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                OutlineSoft.copy(alpha = 0.4f)
            }
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

private fun formatCurrencies(currencies: List<CurrencyInfo>): String {
    if (currencies.isEmpty()) return "—"
    return currencies.joinToString { c ->
        buildString {
            append(c.code)
            append(" (")
            append(c.symbol)
            append(" ")
            append(c.name)
            append(")")
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun CountryDetailContentPreview() {
    CountriesTheme {
        CountryDetailContent(
            country = Country(
                cca3 = "IRL",
                commonName = "Ireland",
                officialName = "Republic of Ireland",
                capital = "Dublin",
                region = "Europe",
                subregion = "Northern Europe",
                population = 4_994_724,
                area = 70_273.0,
                languages = listOf("English", "Irish"),
                carSide = "left",
                currencies = listOf(
                    CurrencyInfo("EUR", "Euro", "€")
                ),
                timezones = listOf("UTC"),
                flagUrl = null,
                coatOfArmsUrl = null
            )
        )
    }
}
