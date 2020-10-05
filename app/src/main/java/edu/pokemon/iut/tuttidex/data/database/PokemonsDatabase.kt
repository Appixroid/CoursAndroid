package edu.pokemon.iut.tuttidex.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.pokemon.iut.tuttidex.BuildConfig
import edu.pokemon.iut.tuttidex.data.database.dao.PokemonDao
import edu.pokemon.iut.tuttidex.data.database.entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = BuildConfig.VERSION_DB, exportSchema = false)
abstract class PokemonsDatabase : RoomDatabase() {
    abstract val pokemonDao: PokemonDao
}