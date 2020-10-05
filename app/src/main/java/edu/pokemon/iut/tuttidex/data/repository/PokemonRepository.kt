package edu.pokemon.iut.tuttidex.data.repository

import androidx.lifecycle.LiveData
import edu.pokemon.iut.tuttidex.data.Pokemon

interface PokemonInsertCallBack {
    fun onInsert(idInserted: Long)
}

interface PokemonRepository {
    fun getPokemon(id: Int): LiveData<Pokemon?>
    fun search(query: String?): LiveData<List<Pokemon>>
    fun filterCaptured(): LiveData<List<Pokemon>>
    fun getIdMaxPokemonLiveData(): LiveData<Int>
    suspend fun createPokemon(
        number: Int,
        name: String,
        baseExp: Int,
        height: Int,
        weight: Int,
        types: String,
        image: String,
        callback: PokemonInsertCallBack
    )

    suspend fun capturePokemon(id: Int?)
    suspend fun refreshPokemons()
    fun getPokemons(): LiveData<List<Pokemon>>
}