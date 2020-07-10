package com.ricardojrsousa.movook.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ricardojrsousa.movook.core.data.Movie
import com.ricardojrsousa.movook.core.data.MovieDetails
import com.ricardojrsousa.movook.framework.UseCases
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val useCases: UseCases) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("Exception HANDLER", throwable.localizedMessage)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + exceptionHandler)

    private val _upcomingMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    val upcomingMovies: LiveData<List<Movie>> = _upcomingMovies

    private val _movieDetails: MutableLiveData<MovieDetails> = MutableLiveData()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    init {
        coroutineScope.launch {
            val list = useCases.getUpcomingMovies.invoke(1)
            _upcomingMovies.postValue(list)
        }
    }

    fun onMovieSelected(movie: Movie) {
        coroutineScope.launch {
            val movieDetails = useCases.getMovieDetails.invoke(movie.id)
            _movieDetails.postValue(movieDetails)
        }
    }

}