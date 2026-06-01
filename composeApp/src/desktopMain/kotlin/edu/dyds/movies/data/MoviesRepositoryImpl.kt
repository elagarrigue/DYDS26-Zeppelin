package edu.dyds.movies.data

import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.data.external.MovieDetailsExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val popularMoviesExternalSource: PopularMoviesExternalSource,
    private val movieDetailsExternalSource: MovieDetailsExternalSource,
    private val localMoviesSource: MoviesLocalSource
): MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return localMoviesSource.getPopularMovies().ifEmpty {
            try {
                val movies = popularMoviesExternalSource.getPopularMovies()
                localMoviesSource.savePopularMovies(movies)
                movies
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieByTitle(title: String): Movie? =
        movieDetailsExternalSource.getMovieByTitle(title)
}
