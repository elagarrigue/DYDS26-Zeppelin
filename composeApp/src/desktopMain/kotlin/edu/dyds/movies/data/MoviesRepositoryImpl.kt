package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesRemoteDataSource
import edu.dyds.movies.data.local.MoviesLocalDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val moviesLocalDataSource: MoviesLocalDataSource
): MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return moviesLocalDataSource.getPopularMovies().ifEmpty {
            try {
                val mappedMovies = moviesRemoteDataSource.getPopularMovies().toDomainMovieList()
                moviesLocalDataSource.savePopularMovies(mappedMovies)
                mappedMovies
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        return try {
            moviesRemoteDataSource.getMovieDetails(id).toDomainMovie()
        } catch (_: Exception) {
            null
        }
    }
}
