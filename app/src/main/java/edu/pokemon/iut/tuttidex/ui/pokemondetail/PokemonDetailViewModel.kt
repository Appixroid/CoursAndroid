package edu.pokemon.iut.tuttidex.ui.pokemondetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import edu.pokemon.iut.tuttidex.data.repository.PokemonRepository
import edu.pokemon.iut.tuttidex.ui.model.Pokemon
import edu.pokemon.iut.tuttidex.ui.model.asUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PokemonDetailViewModel(pokemonId: Int, private val pokemonRepository: PokemonRepository) : ViewModel() {
    private var pokemon: LiveData<Pokemon?> = Transformations.map(pokemonRepository.getPokemon(pokemonId)){
        it?.asUI()
    }

    val number: LiveData<Int>
    val name: LiveData<String>
    val image: LiveData<String>
    val isCaptured: LiveData<Boolean>
    val height: LiveData<Float>
    val weight: LiveData<Float>
    val baseExp: LiveData<Int>
    val types: LiveData<List<String>>


    private var job = SupervisorJob()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    init {
        this.number = Transformations.map(pokemon) {
            it?.number
        }
        this.name = Transformations.map(pokemon) {
            it?.name
        }
        this.image = Transformations.map(pokemon) {
            it?.imageUrl
        }
        this.isCaptured = Transformations.map(pokemon) {
            it?.isCaptured
        }
        this.height = Transformations.map(pokemon) {
            it?.height
        }
        this.weight = Transformations.map(pokemon) {
            it?.weight
        }
        this.baseExp = Transformations.map(pokemon) {
            it?.baseExp
        }
        this.types = Transformations.map(pokemon) {
            it?.types?.split(",")
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}