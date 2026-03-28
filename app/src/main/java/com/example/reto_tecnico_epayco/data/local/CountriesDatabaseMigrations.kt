package com.example.reto_tecnico_epayco.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object CountriesDatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS favorites")
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS favorite_countries (
                    cca3 TEXT NOT NULL PRIMARY KEY,
                    commonName TEXT NOT NULL,
                    officialName TEXT NOT NULL,
                    capital TEXT NOT NULL,
                    region TEXT NOT NULL,
                    subregion TEXT,
                    population INTEGER NOT NULL,
                    area REAL,
                    languagesJson TEXT NOT NULL,
                    carSide TEXT NOT NULL,
                    currenciesJson TEXT NOT NULL,
                    timezonesJson TEXT NOT NULL,
                    flagUrl TEXT,
                    coatOfArmsUrl TEXT,
                    addedAtMillis INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }
    }
}
