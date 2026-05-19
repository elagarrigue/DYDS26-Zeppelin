package edu.dyds.movies.data

import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val popularMoviesExternalSource: PopularMoviesExternalSource,
    private val movieDetailExternalSource: MovieDetailExternalSource,
    private val localMoviesDataSource: MoviesLocalSource
): MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return localMoviesDataSource.getPopularMovies().ifEmpty {
            try {
                val movies = popularMoviesExternalSource.getPopularMovies()
                localMoviesDataSource.savePopularMovies(movies)
                movies
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieByTitle(title: String): Movie? {
        return try {
            movieDetailExternalSource.getMovieByTitle(title)
        } catch (_: Exception) {
            null
        }
    }
}
