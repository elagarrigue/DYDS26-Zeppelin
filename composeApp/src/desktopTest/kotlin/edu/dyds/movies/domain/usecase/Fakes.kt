package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class FakeMoviesRepository(
    private val popularMovies: List<Movie> = emptyList(),
    private val movieDetails: Movie? = null,
) : MoviesRepository {
    var getPopularMoviesCalls = 0
    var getMovieDetailsCalls = 0

    override suspend fun getPopularMovies(): List<Movie> {
        getPopularMoviesCalls += 1
        return popularMovies
    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        getMovieDetailsCalls += 1
        return movieDetails
    }
}
