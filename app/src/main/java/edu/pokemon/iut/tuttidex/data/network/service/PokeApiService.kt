package edu.pokemon.iut.tuttidex.data.network.service

import edu.pokemon.iut.tuttidex.data.network.model.PokemonObject
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Since we only have two service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

/**
 * A retrofit service to fetch a pokemon from pokeApi by ID
 */
interface PokeApiService {
    @GET("pokemon/{pokemonId}")
    suspend fun getPokemon(@Path("pokemonId") pokemonId: Int): Response<PokemonObject>
}

interface PokeApiImageService {
    @GET("sprites/pokemon/{pokemonId}.png")
    suspend fun getPokemonImage(@Path("pokemonId") pokemonId: Int): Response<ResponseBody>
}