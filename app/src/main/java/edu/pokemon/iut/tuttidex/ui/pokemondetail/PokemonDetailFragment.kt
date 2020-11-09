package edu.pokemon.iut.tuttidex.ui.pokemondetail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import edu.pokemon.iut.tuttidex.R
import edu.pokemon.iut.tuttidex.common.image.loadImageWithTransition
import edu.pokemon.iut.tuttidex.databinding.FragmentPokemonDetailBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


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

        viewModel =
            getViewModel { parametersOf(if (arguments != null) PokemonDetailFragmentArgs.fromBundle(arguments!!).pokemonId else 1) }

        binding.pokemonDetailViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.image.observe(this, Observer { image ->
            loadImageWithTransition(
                binding.ivPokemonLogo,
                image,
                this@PokemonDetailFragment
            )
        })
        viewModel.types.observe(this, Observer { types ->
            loadTypes(types)
        })

        //TODO 17) Vous allez faire comme pour le PokemonListFragment et setter les optionsMenu à vrai pour ce fragment
        setHasOptionsMenu(true)

        return binding.root
    }

    @SuppressLint("InflateParams")
    private fun loadTypes(types: List<String>) {
        val context = context
        binding.llPokemonTypes.removeAllViews()
        for (type in types) {
            val pokemonLine = layoutInflater.inflate(R.layout.type_line_view, null)

            val pokemonType: TextView = pokemonLine.findViewById(R.id.tv_type_pokemon)
            pokemonType.text = type

            val pokemonTypeColor: ImageView = pokemonLine.findViewById(R.id.iv_type_pokemon)

            if (context != null) {
                try {
                    val resId = resources.getIdentifier(type, "color", context.packageName)
                    val color = ContextCompat.getColor(context, resId)
                    pokemonTypeColor.setBackgroundColor(color)

                } catch (e: Resources.NotFoundException) {
                    Timber.e(e)
                    pokemonTypeColor.setBackgroundColor(ContextCompat.getColor(context, R.color.defaultType))
                }
            }
            binding.llPokemonTypes.addView(pokemonLine)
        }
    }

    //TODO 18) On recommence avec l'override de la methode pour "gonfler/inflate le menu"
    // Cette fois-ci c'est pokemon_detail_menu.xml que l'on "gonfle"
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pokemon_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //TODO 19) Dans onOptionsItemSelected android nous indique en parametre quelle bouton du menu a été clique
    // on vérifie l'itemId de l'item et si il est égale à celui de "share" cf l'id du menu.xml on appel shareSuccess()
        if(item.itemId == R.id.share)
        {
            shareSuccess()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun shareSuccess() {
        //TODO 20) Lancer une activity en passant en parametre getSharedIntent()
        startActivity(getSharedIntent())
    }

    private fun getSharedIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.number, viewModel.number.value))

        return shareIntent
    }
}