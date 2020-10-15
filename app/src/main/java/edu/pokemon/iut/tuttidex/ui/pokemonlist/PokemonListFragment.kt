package edu.pokemon.iut.tuttidex.ui.pokemonlist


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pokemon.iut.tuttidex.R
import edu.pokemon.iut.tuttidex.databinding.FragmentPokemonListBinding
import edu.pokemon.iut.tuttidex.ui.model.Pokemon
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class PokemonListFragment : Fragment() {
    private lateinit var binding: FragmentPokemonListBinding
    val viewModel: PokemonListViewModel by viewModel()

    private var maxId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pokemon_list,
            container,
            false
        )

        val adapter = PokemonsAdapter(viewModel)
        binding.rvPokemonList.adapter = adapter
        binding.rvPokemonList.layoutManager = LinearLayoutManager(context)
        viewModel.pokemons.observe(viewLifecycleOwner, Observer<List<Pokemon>> { pokemons ->
            pokemons?.apply {
                adapter.submitList(pokemons)
            }
        })

        viewModel.maxPokemonId.observe(viewLifecycleOwner, Observer { maxId ->
            this.maxId = maxId
        })

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.lastPokemonIdViewed in 1 until maxId) {
            binding.rvPokemonList.scrollToPosition(viewModel.lastPokemonIdViewed)
        }

        if(viewModel.lastSearch.isNotBlank()){
            viewModel.search(viewModel.lastSearch)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = view?.findNavController()
        return if (navController != null) {
            NavigationUI.onNavDestinationSelected(item, navController)
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
