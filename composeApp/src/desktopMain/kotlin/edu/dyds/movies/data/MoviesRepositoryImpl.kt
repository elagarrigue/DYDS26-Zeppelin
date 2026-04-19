package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMoviesDataSource
import edu.dyds.movies.data.local.LocalMoviesDataSource
import edu.dyds.movies.data.strategy.error.ErrorHandlingStrategy
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val remoteMoviesDataSource: RemoteMoviesDataSource,
    private val localMoviesDataSource: LocalMoviesDataSource,
    private val popularMoviesErrorStrategy: ErrorHandlingStrategy<List<Movie>>,
    private val movieDetailsErrorStrategy: ErrorHandlingStrategy<Movie?>
) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return localMoviesDataSource.getPopularMovies().ifEmpty {
            popularMoviesErrorStrategy.run {
                val mappedMovies = remoteMoviesDataSource.getPopularMovies().toDomainMovieList()
                localMoviesDataSource.savePopularMovies(mappedMovies)
                mappedMovies
            }
        }
    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        return movieDetailsErrorStrategy.run {
            remoteMoviesDataSource.getMovieDetails(id).toDomainMovie()
        }
    }
}
