package edu.dyds.movies.data

import edu.dyds.movies.data.external.PopularMoviesDataSource
import edu.dyds.movies.data.external.MovieDetailDataSource
import edu.dyds.movies.data.local.LocalMoviesDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val popularMoviesDataSource: PopularMoviesDataSource,
    private val movieDetailDataSource: MovieDetailDataSource,
    private val localMoviesDataSource: LocalMoviesDataSource
): MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return localMoviesDataSource.getPopularMovies().ifEmpty {
            try {
                val mappedMovies = popularMoviesDataSource.getPopularMovies().toDomainMovieList()
                localMoviesDataSource.savePopularMovies(mappedMovies)
                mappedMovies
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieByTitle(title: String): Movie? {
        return try {
            movieDetailDataSource.getMovieByTitle(title).toDomainMovie()
        } catch (_: Exception) {
            null
        }
    }
}
