package com.ricardojrsousa.movook.framework

import android.content.Context
import com.ricardojrsousa.movook.core.data.*
import com.ricardojrsousa.movook.core.repository.BooksDataSource
import com.ricardojrsousa.movook.core.repository.MoviesDataSource
import com.ricardojrsousa.movook.framework.api.BooksClient
import com.ricardojrsousa.movook.framework.api.MoviesClient
import com.ricardojrsousa.movook.framework.db.DatabaseService
import com.ricardojrsousa.movook.framework.db.MovieEntity
import com.ricardojrsousa.movook.utils.filterAdult

/**
 * Created by ricardosousa on 25/05/2020
 */
class DataSource(context: Context) : MoviesDataSource, BooksDataSource {

    private val movieDao = DatabaseService.getInstance(context).movieDao()
    private val movieService = MoviesClient.apiService

    private val bookService = BooksClient.apiService

    override suspend fun getMoviesInTheatres(page: Int): MovieWrapper {
        val movieWrapper = movieService.getMoviesInTheatres(page)
        movieWrapper.results.filterAdult().forEach { movieDao.addMovieEntity(MovieEntity.fromMovie(it)) }
        return movieWrapper
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetails {
        val credits = movieService.getMovieCast(movieId).cast.filter { !it.adult }
        val keywords = movieService.getMovieKeywords(movieId).keywords
        val movieDetails = movieService.getMovieDetails(movieId)
        movieDetails.credits = credits
        movieDetails.keywords = keywords
        return movieDetails
    }

    override suspend fun getSimilarMovies(movieId: String, page: Int): MovieWrapper {
        val movieWrapper = movieService.getSimilarMovies(movieId, page)
        movieWrapper.results.filterAdult().forEach { movieDao.addMovieEntity(MovieEntity.fromMovie(it)) }
        return movieWrapper
    }

    override suspend fun getPersonDetails(personId: String): Person {
        val personDetails = movieService.getPersonDetails(personId)
        val personCredits = movieService.getPersonMovieCredits(personId)
        personDetails.credits = personCredits.apply { this.movies = this.movies.filterAdult().sortedByDescending { it.voteAverage } }
        return personDetails
    }

    override suspend fun searchBooksByTitle(query: String): List<Book> {
        val books = bookService.searchBooksByTitle(query)
        //TODO: Save on db
        return books.items
    }

    override suspend fun getBookDetails(id: String): Book {
        val book = bookService.getBookById(id)
        //TODO: Save on db
        return book
    }


}