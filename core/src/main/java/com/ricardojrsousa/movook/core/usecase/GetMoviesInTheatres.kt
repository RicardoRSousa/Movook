package com.ricardojrsousa.movook.core.usecase

import com.ricardojrsousa.movook.core.repository.MoviesRepository

/**
 * Created by ricardosousa on 21/05/2020
 */
class GetMoviesInTheatres(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(page: Int) = moviesRepository.getMoviesInTheatres(page)
}