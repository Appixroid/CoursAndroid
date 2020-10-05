package edu.pokemon.iut.tuttidex.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import edu.pokemon.iut.tuttidex.data.Pokemon
import edu.pokemon.iut.tuttidex.data.network.model.PokemonObject
import edu.pokemon.iut.tuttidex.data.network.model.asEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.FileOutputStream

class PokemonRepositoryImpl(
    private val dao: edu.pokemon.iut.tuttidex.data.database.dao.PokemonDao,
    private val pokeApiService: edu.pokemon.iut.tuttidex.data.network.service.PokeApiService,
    private val pokeApiImageService: edu.pokemon.iut.tuttidex.data.network.service.PokeApiImageService,
    private val context: Context
) : PokemonRepository {

    override fun getPokemons(): LiveData<List<Pokemon>> {
        return Transformations.map(dao.getAllPokemon()) {
            it.map { pokemon ->
                Pokemon(
                    number = pokemon.id,
                    name = pokemon.name,
                    imageUrl = pokemon.image,
                    types = pokemon.types,
                    weight = pokemon.weight.times(0.1f),
                    height = pokemon.height.times(0.1f),
                    baseExp = pokemon.baseExperience,
                    isCaptured = pokemon.isCaptured
                )
            }
        }
    }

    override fun getPokemon(id: Int): LiveData<Pokemon?> {
        return Transformations.map(dao.getPokemonLiveData(id)) {
            Pokemon(
                number = it.id,
                name = it.name,
                imageUrl = it.image,
                types = it.types,
                weight = it.weight.times(0.1f),
                height = it.height.times(0.1f),
                baseExp = it.baseExperience,
                isCaptured = it.isCaptured
            )
        }
    }


    override fun search(query: String?): LiveData<List<Pokemon>> {
        return Transformations.map(dao.getPokemonsByQuery("%".plus(query).plus("%"))) {
            it.map { pokemon ->
                Pokemon(
                    number = pokemon.id,
                    name = pokemon.name,
                    imageUrl = pokemon.image,
                    types = pokemon.types,
                    weight = pokemon.weight.times(0.1f),
                    height = pokemon.height.times(0.1f),
                    baseExp = pokemon.baseExperience,
                    isCaptured = pokemon.isCaptured
                )
            }
        }
    }

    override fun filterCaptured(): LiveData<List<Pokemon>> {
        return Transformations.map(dao.getPokemonCaptured()) {
            it.map { pokemon ->
                Pokemon(
                    number = pokemon.id,
                    name = pokemon.name,
                    imageUrl = pokemon.image,
                    types = pokemon.types,
                    weight = pokemon.weight.times(0.1f),
                    height = pokemon.height.times(0.1f),
                    baseExp = pokemon.baseExperience,
                    isCaptured = pokemon.isCaptured
                )
            }
        }
    }

    override fun getIdMaxPokemonLiveData(): LiveData<Int> {
        return dao.getIdMaxPokemonLiveData()
    }

    override suspend fun createPokemon(
        number: Int,
        name: String,
        baseExp: Int,
        height: Int,
        weight: Int,
        types: String,
        image: String,
        callback: PokemonInsertCallBack
    ) {
        withContext(Dispatchers.IO) {
            val pokemonEntity = edu.pokemon.iut.tuttidex.data.database.entity.PokemonEntity(
                number,
                name,
                baseExp,
                height,
                weight,
                types,
                image,
                false
            )
            val idInserted = dao.insert(pokemonEntity)
            callback.onInsert(idInserted)
        }
    }

    override suspend fun capturePokemon(id: Int?) {
        withContext(Dispatchers.IO) {
            if (id != null) {
                val pokemonEntity = dao.getPokemon(id)
                if (pokemonEntity != null) {
                    pokemonEntity.isCaptured = !pokemonEntity.isCaptured
                    dao.update(pokemonEntity)
                }
            }
        }
    }

    override suspend fun refreshPokemons() {
        for (i in 1..807)
            withContext(Dispatchers.IO) {
                val pokemonEntity = dao.getPokemon(i)

                if (pokemonEntity == null || pokemonEntity.image.isBlank()) {
                    val response = pokeApiService.getPokemon(i)
                    if (response.isSuccessful && response.body() != null) {
                        val responseImage = pokeApiImageService.getPokemonImage(i)
                        val fileName = StringBuilder().append(i).append(".png").toString()
                        insertPokemon(responseImage, fileName, response)
                    }
                }
            }
    }

    private fun insertPokemon(
        responseImage: Response<ResponseBody>,
        fileName: String,
        response: Response<PokemonObject>
    ) {
        if (responseImage.isSuccessful && responseImage.body() != null) {
            val outputStream: FileOutputStream
            try {
                outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                outputStream.write(responseImage.body()!!.bytes())
                outputStream.close()
                dao.insert(response.body()!!.asEntity(context.filesDir.path.plus("/").plus(fileName)))
            } catch (error: Exception) {
                dao.insert(response.body()!!.asEntity(""))
            }
        } else {
            dao.insert(response.body()!!.asEntity(""))
        }
    }
}



