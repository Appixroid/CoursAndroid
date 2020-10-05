package edu.pokemon.iut.tuttidex.ui.pokemonlist

import androidx.recyclerview.widget.DiffUtil
import edu.pokemon.iut.tuttidex.R
import edu.pokemon.iut.tuttidex.common.recyclerview.DataBindingAdapter
import edu.pokemon.iut.tuttidex.ui.model.Pokemon

class PokemonsAdapter(viewModel: PokemonListViewModel?) : DataBindingAdapter<Pokemon, PokemonListViewModel>(DiffCallback(),viewModel){

    class DiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return (oldItem.name == newItem.name
                    && oldItem.imageUrl == newItem.imageUrl
                    && oldItem.isCaptured == newItem.isCaptured)
        }
    }

    override fun getItemViewType(position: Int) = R.layout.component_pokemon_line
}