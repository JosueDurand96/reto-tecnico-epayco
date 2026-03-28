package com.example.reto_tecnico_epayco.data.mapper

import com.example.reto_tecnico_epayco.data.remote.dto.CountryDto
import com.example.reto_tecnico_epayco.domain.model.Country
import com.example.reto_tecnico_epayco.domain.model.CurrencyInfo
import javax.inject.Inject

class CountryMapper @Inject constructor() {

    fun toDomain(dto: CountryDto): Country {
        val cca3 = dto.cca3.orEmpty().ifEmpty { "UNK" }
        val common = dto.name?.common.orEmpty()
        val official = dto.name?.official.orEmpty()
        val capital = dto.capital?.firstOrNull().orEmpty()

        val languages = dto.languages?.values?.sorted().orEmpty()

        val currencies = dto.currencies?.mapNotNull { (code, cur) ->
            val name = cur.name ?: return@mapNotNull null
            CurrencyInfo(
                code = code,
                name = name,
                symbol = cur.symbol.orEmpty()
            )
        }.orEmpty()

        val carSide = dto.car?.side.orEmpty()

        val flagUrl = dto.flags?.png ?: dto.flags?.svg
        val armsUrl = dto.coatOfArms?.png ?: dto.coatOfArms?.svg

        return Country(
            cca3 = cca3,
            commonName = common,
            officialName = official,
            capital = capital,
            region = dto.region.orEmpty(),
            subregion = dto.subregion,
            population = dto.population ?: 0L,
            area = dto.area,
            languages = languages,
            carSide = carSide,
            currencies = currencies,
            timezones = dto.timezones.orEmpty(),
            flagUrl = flagUrl,
            coatOfArmsUrl = armsUrl
        )
    }
}
