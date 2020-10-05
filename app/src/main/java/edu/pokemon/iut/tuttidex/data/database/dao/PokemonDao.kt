package edu.pokemon.iut.tuttidex.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import edu.pokemon.iut.tuttidex.data.database.entity.PokemonEntity

@Dao
interface PokemonDao {

    @Insert(onConflict = REPLACE)
    fun insert(pokemon: PokemonEntity):Long

    @Update(onConflict = REPLACE)
    fun update(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_table ORDER BY id ASC")
    fun getAllPokemon(): LiveData<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE pokemon_name LIKE :query " +
            "OR pokemon_types LIKE :query " +
            "OR pokemon_weight LIKE :query " +
            "OR pokemon_height LIKE :query " +
            "OR id LIKE :query " +
            "ORDER BY id ASC")
    fun getPokemonsByQuery(query: String?): LiveData<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE pokemon_is_captured = 1 ORDER BY id ASC")
    fun getPokemonCaptured():LiveData<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE id = :id")
    fun getPokemonLiveData(id: Int):LiveData<PokemonEntity>

    @Query("SELECT * FROM pokemon_table WHERE id = :id")
    fun getPokemon(id: Int): PokemonEntity?

    @Query("SELECT EXISTS (SELECT * FROM pokemon_table WHERE id = :id)")
    fun hasPokemon(id: Int):Boolean

    @Query("SELECT COUNT(*) FROM pokemon_table")
    fun getIdMaxPokemonLiveData(): LiveData<Int>
}