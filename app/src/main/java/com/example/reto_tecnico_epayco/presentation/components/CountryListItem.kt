package com.example.reto_tecnico_epayco.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.reto_tecnico_epayco.R
import com.example.reto_tecnico_epayco.domain.model.Country
import com.example.reto_tecnico_epayco.ui.theme.CountriesTheme
import com.example.reto_tecnico_epayco.ui.theme.OutlineSoft

@Composable
fun CountryListItem(
    country: Country,
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, OutlineSoft.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FlagImage(
                url = country.flagUrl,
                contentDescription = stringResource(R.string.cd_flag, country.commonName),
                modifier = Modifier.size(width = 56.dp, height = 38.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            CoatOfArmsThumb(
                url = country.coatOfArmsUrl,
                contentDescription = stringResource(R.string.cd_coat_of_arms, country.commonName),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = country.commonName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = country.officialName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = country.capital.ifEmpty { stringResource(R.string.no_capital) },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (isFavorite) {
                Icon(
                    imageVector = Icons.Rounded.Bookmark,
                    contentDescription = stringResource(R.string.cd_favorite_marker),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun FlagImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val model = ImageRequest.Builder(context)
        .data(url)
        .crossfade(true)
        .build()
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, OutlineSoft.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun CoatOfArmsThumb(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, OutlineSoft.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(36.dp)
                .padding(2.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CountryListItemPreview() {
    CountriesTheme {
        CountryListItem(
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
                currencies = emptyList(),
                timezones = listOf("UTC"),
                flagUrl = null,
                coatOfArmsUrl = null
            ),
            isFavorite = true,
            onClick = {}
        )
    }
}
