package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

object LocalMoviesDataSourceImpl: LocalMoviesDataSource {
    private val popularMoviesCache = mutableListOf<Movie>()

    override fun getPopularMovies(): List<Movie> = popularMoviesCache.toList()

    override fun savePopularMovies(movies: List<Movie>) {
        popularMoviesCache.clear()
        popularMoviesCache.addAll(movies)
    }
}

