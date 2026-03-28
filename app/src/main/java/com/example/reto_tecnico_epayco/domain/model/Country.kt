package com.example.reto_tecnico_epayco.domain.model

data class Country(
    val cca3: String,
    val commonName: String,
    val officialName: String,
    val capital: String,
    val region: String,
    val subregion: String?,
    val population: Long,
    val area: Double?,
    val languages: List<String>,
    val carSide: String,
    val currencies: List<CurrencyInfo>,
    val timezones: List<String>,
    val flagUrl: String?,
    val coatOfArmsUrl: String?
)
