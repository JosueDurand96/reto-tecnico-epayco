package com.example.reto_tecnico_epayco.data.mapper

import com.example.reto_tecnico_epayco.data.local.entity.FavoriteCountryEntity
import com.example.reto_tecnico_epayco.domain.model.Country
import com.example.reto_tecnico_epayco.domain.model.CurrencyInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteCountryEntityMapper @Inject constructor(
    private val gson: Gson
) {

    fun toEntity(country: Country): FavoriteCountryEntity =
        FavoriteCountryEntity(
            cca3 = country.cca3,
            commonName = country.commonName,
            officialName = country.officialName,
            capital = country.capital,
            region = country.region,
            subregion = country.subregion,
            population = country.population,
            area = country.area,
            languagesJson = gson.toJson(country.languages),
            carSide = country.carSide,
            currenciesJson = gson.toJson(country.currencies),
            timezonesJson = gson.toJson(country.timezones),
            flagUrl = country.flagUrl,
            coatOfArmsUrl = country.coatOfArmsUrl
        )

    fun toDomain(entity: FavoriteCountryEntity): Country {
        val languagesType = object : TypeToken<List<String>>() {}.type
        val currenciesType = object : TypeToken<List<CurrencyInfo>>() {}.type
        val timezonesType = object : TypeToken<List<String>>() {}.type
        return Country(
            cca3 = entity.cca3,
            commonName = entity.commonName,
            officialName = entity.officialName,
            capital = entity.capital,
            region = entity.region,
            subregion = entity.subregion,
            population = entity.population,
            area = entity.area,
            languages = gson.fromJson(entity.languagesJson, languagesType) ?: emptyList(),
            carSide = entity.carSide,
            currencies = gson.fromJson(entity.currenciesJson, currenciesType) ?: emptyList(),
            timezones = gson.fromJson(entity.timezonesJson, timezonesType) ?: emptyList(),
            flagUrl = entity.flagUrl,
            coatOfArmsUrl = entity.coatOfArmsUrl
        )
    }
}
