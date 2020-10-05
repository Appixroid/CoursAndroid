package edu.pokemon.iut.tuttidex.common.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDataSourceModule = module {
    // Room Database
    single {
        Room.databaseBuilder(androidApplication(), edu.pokemon.iut.tuttidex.data.database.PokemonsDatabase::class.java, "pokemon_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    // Expose PokemonDAO
    single { get<edu.pokemon.iut.tuttidex.data.database.PokemonsDatabase>().pokemonDao }
}