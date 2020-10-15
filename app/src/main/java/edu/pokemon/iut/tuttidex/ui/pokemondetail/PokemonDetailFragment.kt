package edu.pokemon.iut.tuttidex.ui.pokemondetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import edu.pokemon.iut.tuttidex.R
import edu.pokemon.iut.tuttidex.common.image.loadImageWithOutTransition
import edu.pokemon.iut.tuttidex.databinding.FragmentPokemonDetailBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class PokemonDetailFragment : Fragment() {

    private lateinit var viewModel: PokemonDetailViewModel
    private lateinit var binding: FragmentPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        //The sharedElement between screens is only available for version above of LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pokemon_detail,
            container,
            false
        )


        var pokemonId = 1

        //TODO 4) Recuperer l'id du pokemon contenu dans les "arguments" du fragment, enregistrer le dans la variable pokemonId
        // Lancer l'application et vérifier que vous naviguer
        pokemonId = this.arguments?.get("pokemonId") as Int

        viewModel =
            getViewModel { parametersOf(pokemonId) }

        binding.pokemonDetailViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.image.observe(this, Observer { image ->
            //TODO 16) Lorsque l'url de l'image du pokemon est disponible la donnée sera mise à disposition ici
            // Utiliser la pour charger l'image dans l'ImageView du layout (utiliser loadImageWithOutTransition)
            loadImageWithOutTransition(container!!.findViewById(R.id.detail_image), image)
        })
        viewModel.types.observe(this, Observer { types ->
            //TODO 17) Plutôt que de n'afficher qu'un seul type essayer de trouver une solution pour tous les afficher cf TODO 18
            loadTypes(types)
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    @SuppressLint("InflateParams")
    private fun loadTypes(types: List<String>) {
        //TODO 18) coder ici le todo 17, petit indice un LinearLayout peut vous aider
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.pokemon_detail_menu, menu)
        val activity = activity
        if (activity != null && null == getSharedIntent().resolveActivity(activity.packageManager)) {
            menu.findItem(R.id.share)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareSuccess() {
        startActivity(getSharedIntent())
    }

    private fun getSharedIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.number, viewModel.number.value))

        return shareIntent
    }
}