package com.example.reto_tecnico_epayco.di

import android.content.Context
import androidx.room.Room
import com.example.reto_tecnico_epayco.data.local.CountriesDatabase
import com.example.reto_tecnico_epayco.data.local.CountriesDatabaseMigrations
import com.example.reto_tecnico_epayco.data.local.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CountriesDatabase =
        Room.databaseBuilder(
            context,
            CountriesDatabase::class.java,
            "countries.db"
        )
            .addMigrations(CountriesDatabaseMigrations.MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: CountriesDatabase): FavoriteDao = db.favoriteDao()
}
