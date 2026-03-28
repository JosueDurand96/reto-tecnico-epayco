package com.example.reto_tecnico_epayco.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reto_tecnico_epayco.R
import com.example.reto_tecnico_epayco.presentation.navigation.Routes
import com.example.reto_tecnico_epayco.ui.theme.CountriesTheme

@Composable
fun CountriesBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        tonalElevation = 3.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == Routes.COUNTRIES,
            onClick = { onNavigate(Routes.COUNTRIES) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Public,
                    contentDescription = stringResource(R.string.cd_tab_countries)
                )
            },
            label = { Text(stringResource(R.string.tab_countries)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        NavigationBarItem(
            selected = currentRoute == Routes.FAVORITES,
            onClick = { onNavigate(Routes.FAVORITES) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = stringResource(R.string.cd_tab_favorites)
                )
            },
            label = { Text(stringResource(R.string.tab_favorites)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CountriesBottomBarPreview() {
    CountriesTheme {
        CountriesBottomBar(
            currentRoute = Routes.COUNTRIES,
            onNavigate = {}
        )
    }
}
