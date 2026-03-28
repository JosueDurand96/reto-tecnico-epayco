package com.example.reto_tecnico_epayco.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.reto_tecnico_epayco.data.local.entity.FavoriteCountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT cca3 FROM favorite_countries ORDER BY addedAtMillis DESC")
    fun observeFavoriteCca3Ordered(): Flow<List<String>>

    @Query("SELECT * FROM favorite_countries ORDER BY addedAtMillis DESC")
    suspend fun getFavoritesOrdered(): List<FavoriteCountryEntity>

    @Query("SELECT * FROM favorite_countries WHERE cca3 = :cca3 LIMIT 1")
    suspend fun getByCca3(cca3: String): FavoriteCountryEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_countries WHERE cca3 = :cca3 LIMIT 1)")
    suspend fun isFavorite(cca3: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteCountryEntity)

    @Query("DELETE FROM favorite_countries WHERE cca3 = :cca3")
    suspend fun deleteByCca3(cca3: String)
}
