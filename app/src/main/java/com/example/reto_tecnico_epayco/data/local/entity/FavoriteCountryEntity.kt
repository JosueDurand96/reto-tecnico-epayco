package com.example.reto_tecnico_epayco.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Copia local del país al marcarlo como favorito para listado y detalle sin red.
 */
@Entity(tableName = "favorite_countries")
data class FavoriteCountryEntity(
    @PrimaryKey val cca3: String,
    val commonName: String,
    val officialName: String,
    val capital: String,
    val region: String,
    val subregion: String?,
    val population: Long,
    val area: Double?,
    val languagesJson: String,
    val carSide: String,
    val currenciesJson: String,
    val timezonesJson: String,
    val flagUrl: String?,
    val coatOfArmsUrl: String?,
    val addedAtMillis: Long = System.currentTimeMillis()
)
