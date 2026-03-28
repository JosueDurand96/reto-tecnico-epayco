package com.example.reto_tecnico_epayco.di

import com.example.reto_tecnico_epayco.data.repository.CountriesRepositoryImpl
import com.example.reto_tecnico_epayco.domain.repository.CountriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCountriesRepository(
        impl: CountriesRepositoryImpl
    ): CountriesRepository
}
