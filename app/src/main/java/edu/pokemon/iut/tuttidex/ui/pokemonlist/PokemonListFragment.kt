package edu.pokemon.iut.tuttidex.ui.pokemonlist


import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.SearchView
import android.widget.Toast
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

        //TODO 5) Configurer le Fragment pour dire qu'il possedra des OptionsMenu
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

        initSearch(menu)

        initFilter(menu)

        super.onCreateOptionsMenu(menu, inflater)
    }
    //TODO 6) Override la méthode de callback nous indiquant que le OptionsMenu est entrain d'être crée
    //TODO 7) Dans la méthode que vous venez d'override "gonfler/inflate votre overflow_menu.xml
    // Vous terminer la méthode avec l'appel à super.XXXXXXXX

    //TODO 8) vous trouverez plus bas dans le code 2 méthodes : initFilter(menu:Menu)  et initSearch(menu : Menu)
    // Appeler les dans la méthode que vous avez override


    private fun initFilter(menu: Menu) {
        //TODO 9) Recuperer un objet de type MenuItem grâce au menu et à l'id de l'item dans votre overflow_menu.xml
        // L'objectif va être de donner un comportement à l'item.
        // Ici on vas commencer le filtrage sur l'état de capture des pokemons
        // On ne veut afficher que les pokemons capturer si l'on clique sur l'item
        //TODO 10) grâce à l'item vous pouvez récuperer une actionView de type CheckBox ( penser a caster avec le mot clé "as")
        // Les actions view ont plusieurs type possible il nous faut caster pour pouvoir accéder aux méthodes qui nous interressent
        //TODO 11) sur l'actionView on va rajouter un OnCheckedChangeListener
        // et dans la méthode que l'interface nous demande d'override nous allons appeler le viewModel et lui dire que l'on filtre par Capturé ou non
        //TODO 12 Bonus) le viewModel vous permet d'observer une liveData pokemonFilter
        // Nous allons pouvoir connaitre la derniere recherche/filtre effectué
        // On force la valeur de filterView.isChecked avec la valeur filterCaptured de "it" pour s'assurer d'avoir le dernier état connue
        // On fait cela pour gérer la changement d'orientation (tester de basculer le telephone et regarder ce qu'il ce passe avant de coder)
        // On peut également imposer de fermer la recherche (closeSearch) si le filtre par capturé est cocher.
        val capturedCheckBox = menu.findItem(R.id.filterByCaptured).actionView as CheckBox
        capturedCheckBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                viewModel.pokemonFilter.postValue(PokemonListViewModel.PokemonFilter("", isChecked))
            }
        })

        viewModel.pokemonFilter.observe(viewLifecycleOwner, Observer {
            capturedCheckBox.isChecked = it.filterCaptured
        })
    }

    private fun initSearch(menu: Menu) {
        //TODO 13) Comme le todo 9 nous allons recuperer un autre MenuItem
        // L'objectif ici va être de faire une recherche par nom d'un ou plusieurs pokemon
        //TODO 14) Idem que todo 10) on recupere une actionView qui cette fois est de type SearchView
        //TODO 15) Idem que todo 11) mais cette fois c'est un OnQueryTextListener que nous allons ajouter à l'actionView
        // L'interface nous demande d'override deux méthodes : onQueryTextSubmit et onQueryTextChange
        // La première est appeler quand le bouton recherche ou le bouton valider du clavier virtuelle est appuyer
        // La deuxieme est appeler à chaque caractéres taper
        // Les deux vous donne en parametre d'entrée le texte taper par l'utilisateur
        // Dans l'une ou l'autre ou les deux vous aller appeler le viewModel et lui indiquer que nous voulons faire une recherche
        // Retourner false dans les deux méthodes pour indiquer à Android que nous ne souhaitons pas appeler d'autre Listener attacher à notre actionView
        //TODO 15 Bonus) Récuperer la value de pokemonFilter dans le viewModel
        // Verifier si elle n'est pas vide et dans ce cas là ouvrer la barre de recherche (expandActionView) et setter la requete (un boolean vous est demandé 'submit')
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return false
            }
        })

        if(viewModel.pokemonFilter.value!!.searchQuery.isNotEmpty())
        {
            menuItem.expandActionView()
            searchView.setQuery(viewModel.pokemonFilter.value!!.searchQuery, true)
        }
    }

    private fun closeSearch(menu: Menu) {
        //TODO 16 Bonus) Ne fait ce bonus que si le Todo 13 Bonus) et Todo 14) à 15) sont fait
        // Vous aller comme dans le 13) et 14) recuperer l'actionView de type SearchView
        // Cette fois-ci on va regarder sur l'item si l'ActionView est deplier (expanded)
        // Si c'est le cas on veut la fermer (collapse) et lui passer une requete vide que nous n'allons pas envoyer

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

        if(menuItem.isActionViewExpanded)
        {
            menuItem.collapseActionView()
            searchView.setQuery("", false)
        }
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
