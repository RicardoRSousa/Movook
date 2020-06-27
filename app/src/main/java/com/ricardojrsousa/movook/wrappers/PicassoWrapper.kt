package com.ricardojrsousa.movook.wrappers

import android.widget.ImageView
import com.ricardojrsousa.movook.R
import com.squareup.picasso.Picasso

/**
 * Created by ricardosousa on 23/03/2020
 */
object PicassoWrapper {

    private val posterPathPrefix = "https://image.tmdb.org/t/p/w500"
    fun loadMoviePoster(url: String, view: ImageView) {
        Picasso.get()
            .load(posterPathPrefix + url)
            .placeholder(R.drawable.poster_placeholder)
            .into(view);
    }

}

fun ImageView.loadMoviePoster(url: String) {
    PicassoWrapper.loadMoviePoster(url, this)
}