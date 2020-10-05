package edu.pokemon.iut.tuttidex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PokemonsDatabaseTest {

    @get:Rule val testRule = InstantTaskExecutorRule()

    private lateinit var pokemonDao: edu.pokemon.iut.tuttidex.data.database.dao.PokemonDao
    private lateinit var db: edu.pokemon.iut.tuttidex.data.database.PokemonsDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, edu.pokemon.iut.tuttidex.data.database.PokemonsDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        pokemonDao = db.pokemonDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetPokemon() {
        val pokemonEntity = edu.pokemon.iut.tuttidex.data.database.entity.PokemonEntity(
            1,
            "Bulbasaur",
            30,
            10,
            20,
            "Grass, Sol",
            "images/1/pok.jpeg"
        )
        pokemonDao.insert(pokemonEntity)
        val pokemon = pokemonDao.getPokemon(1)
        assertEquals(pokemon?.id, 1)
    }
}

