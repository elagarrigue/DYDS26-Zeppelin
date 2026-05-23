package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val moviesExternalSource: MoviesExternalSource,
    private val movieExternalSource: MovieExternalSource,
    private val localMoviesSource: MoviesLocalSource
): MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return localMoviesSource.getPopularMovies().ifEmpty {
            try {
                val movies = moviesExternalSource.getPopularMovies()
                localMoviesSource.savePopularMovies(movies)
                movies
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieByTitle(title: String): Movie? =
        movieExternalSource.getMovieByTitle(title)
}
