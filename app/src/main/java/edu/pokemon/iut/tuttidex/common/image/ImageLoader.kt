package edu.pokemon.iut.tuttidex.common.image

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import edu.pokemon.iut.tuttidex.R

fun loadImageWithTransition(view: ImageView, imageUrl: String?, fragment: Fragment){
    val options = RequestOptions()
        .centerCrop()
        .dontAnimate()
    val context = fragment.context
    if(context != null) {
        Glide.with(context)
            .asDrawable()
            .load(imageUrl)
            .apply(options)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    fragment.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    fragment.startPostponedEnterTransition()
                    return false
                }

            })
            .into(view)
    }
}

@BindingAdapter("imageUrl")
fun loadImageWithOutTransition(view: ImageView, imageUrl: String?) {
    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(R.drawable.ic_launcher_foreground)
        .centerCrop()
    Glide.with(view.context)
        .load(imageUrl)
        .apply(options)
        .into(view)
}

@BindingAdapter("imageUrlNoCache")
fun loadImageWithoutCache(view: ImageView, imageUrl: String?){
    val options = RequestOptions()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(R.drawable.ic_launcher_foreground)
        .centerCrop()
    Glide.with(view.context)
        .load(imageUrl)
        .apply(options)
        .into(view)
}