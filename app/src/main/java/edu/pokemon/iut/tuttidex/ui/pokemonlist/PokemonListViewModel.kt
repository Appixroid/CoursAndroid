package edu.pokemon.iut.tuttidex.ui.pokemonlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import edu.pokemon.iut.tuttidex.data.repository.PokemonRepository
import edu.pokemon.iut.tuttidex.ui.model.Pokemon
import edu.pokemon.iut.tuttidex.ui.model.asUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PokemonListViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {
    private var job = SupervisorJob()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    class PokemonFilter(val searchQuery: String = "", val filterCaptured: Boolean = false)

    val pokemonFilter = MutableLiveData<PokemonFilter>()

    var lastPokemonIdViewed: Int = 0
    var lastSearch: String = ""

    var maxPokemonId: LiveData<Int>

    var pokemons: LiveData<List<Pokemon>> = Transformations.switchMap(pokemonFilter) {
        when {
            pokemonFilter.value?.searchQuery?.isNotBlank() ?: false
            -> Transformations.map(pokemonRepository.search(pokemonFilter.value?.searchQuery)) {
                it.map { pokemon ->
                    pokemon.asUI()
                }
            }
            pokemonFilter.value?.filterCaptured ?: false
            -> Transformations.map(pokemonRepository.filterCaptured()) {
                it.map { pokemon ->
                    pokemon.asUI()
                }
            }
            else
            -> Transformations.map(pokemonRepository.getPokemons()) {
                it.map { pokemon ->
                    pokemon.asUI()
                }
            }
        }
    }

    init {
        uiScope.launch {
            pokemonRepository.refreshPokemons()
        }
        pokemonFilter.value = PokemonFilter("", false)

        maxPokemonId = Transformations.map(pokemonRepository.getIdMaxPokemonLiveData()) {
            it
        }
    }

    fun search(query: String) {
        val filtered = if (query.isNotBlank()) {
            false
        } else {
            pokemonFilter.value?.filterCaptured ?: false
        }
        pokemonFilter.value = PokemonFilter(query, filtered)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun onPokemonClicked(pokemon: Pokemon?) {
        lastPokemonIdViewed = pokemon?.number?.minus(1) ?: 0
        lastSearch = pokemonFilter.value?.searchQuery ?: ""
    }

    fun onCaptured(pokemon: Pokemon?) {
        uiScope.launch {
            if (pokemon != null) {
                pokemonRepository.capturePokemon(pokemon.number)
            }
        }
    }

    fun onFilterByCaptured(checked: Boolean) {
        pokemonFilter.value = PokemonFilter("", checked)
        lastSearch = ""
    }
}
