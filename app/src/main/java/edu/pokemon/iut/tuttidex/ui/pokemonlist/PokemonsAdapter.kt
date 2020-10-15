package edu.pokemon.iut.tuttidex.ui.pokemonlist

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import edu.pokemon.iut.tuttidex.R
import edu.pokemon.iut.tuttidex.common.recyclerview.DataBindingAdapter
import edu.pokemon.iut.tuttidex.common.recyclerview.DataBindingViewHolder
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

    override fun onBindViewHolder(holder: DataBindingViewHolder<Pokemon, PokemonListViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        val pokemon = this.currentList[position]
        holder.itemView.setOnClickListener {
            viewModel?.onPokemonClicked(getItem(holder.adapterPosition))
            //TODO 3) Ajouter un appel au NavController pour "naviguer" vers le PokemonDetailFragment
            // Le fragment attends l'id du pokemon que l'on veut en entrée
            Navigation.findNavController(holder.itemView).navigate(R.id.pokemonDetailFragment, bundleOf("pokemonId" to pokemon.number))
        }
    }

    override fun getItemViewType(position: Int) = R.layout.component_pokemon_line
}