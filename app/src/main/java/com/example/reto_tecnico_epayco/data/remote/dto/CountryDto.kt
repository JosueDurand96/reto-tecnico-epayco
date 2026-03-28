package com.example.reto_tecnico_epayco.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("cca3") val cca3: String?,
    @SerializedName("name") val name: NameDto?,
    @SerializedName("capital") val capital: List<String>?,
    @SerializedName("region") val region: String?,
    @SerializedName("subregion") val subregion: String?,
    @SerializedName("population") val population: Long?,
    @SerializedName("area") val area: Double?,
    @SerializedName("languages") val languages: Map<String, String>?,
    @SerializedName("currencies") val currencies: Map<String, CurrencyDto>?,
    @SerializedName("car") val car: CarDto?,
    @SerializedName("flags") val flags: FlagsDto?,
    @SerializedName("coatOfArms") val coatOfArms: CoatOfArmsDto?,
    @SerializedName("timezones") val timezones: List<String>?
)

data class NameDto(
    @SerializedName("common") val common: String?,
    @SerializedName("official") val official: String?
)

data class CurrencyDto(
    @SerializedName("name") val name: String?,
    @SerializedName("symbol") val symbol: String?
)

data class CarDto(
    @SerializedName("side") val side: String?
)

data class FlagsDto(
    @SerializedName("png") val png: String?,
    @SerializedName("svg") val svg: String?
)

data class CoatOfArmsDto(
    @SerializedName("png") val png: String?,
    @SerializedName("svg") val svg: String?
)
