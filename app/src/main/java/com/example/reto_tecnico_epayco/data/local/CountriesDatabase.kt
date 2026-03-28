package com.example.reto_tecnico_epayco.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reto_tecnico_epayco.data.local.dao.FavoriteDao
import com.example.reto_tecnico_epayco.data.local.entity.FavoriteCountryEntity

@Database(
    entities = [FavoriteCountryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CountriesDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
