package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMoviesDataSource
import edu.dyds.movies.data.local.LocalMoviesDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val remoteMoviesDataSource: RemoteMoviesDataSource,
    private val localMoviesDataSource: LocalMoviesDataSource
): MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return localMoviesDataSource.getPopularMovies().ifEmpty {
            try {
                val mappedMovies = remoteMoviesDataSource.getPopularMovies().toDomainMovieList()
                localMoviesDataSource.savePopularMovies(mappedMovies)
                mappedMovies
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieByTitle(title: String): Movie? {
        return try {
            remoteMoviesDataSource.getMovieByTitle(title).toDomainMovie()
        } catch (_: Exception) {
            null
        }
    }
}
