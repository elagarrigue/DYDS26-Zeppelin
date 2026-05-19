package edu.dyds.movies.data

import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie

class FakeRemoteMoviesExternalSource(
    var popularMoviesResult: List<Movie>? = null,
    var movieByTitleResult: Movie? = null,
    var popularMoviesException: Exception? = null,
    var movieByTitleException: Exception? = null,
) : PopularMoviesExternalSource, MovieDetailExternalSource {
    var getPopularMoviesCalls = 0
    var getMovieByTitleCalls = 0

    override suspend fun getPopularMovies(): List<Movie> {
        getPopularMoviesCalls += 1
        popularMoviesException?.let { throw it }
        return requireNotNull(popularMoviesResult)
    }

    override suspend fun getMovieByTitle(title: String): Movie {
        getMovieByTitleCalls += 1
        movieByTitleException?.let { throw it }
        return requireNotNull(movieByTitleResult)
    }
}

class FakeMoviesLocalSource(
    initialMovies: List<Movie> = emptyList(),
) : MoviesLocalSource {
    private val cache = initialMovies.toMutableList()

    var getPopularMoviesCalls = 0
    var savePopularMoviesCalls = 0
    var lastSaved: List<Movie> = emptyList()

    override fun getPopularMovies(): List<Movie> {
        getPopularMoviesCalls += 1
        return cache.toList()
    }

    override fun savePopularMovies(movies: List<Movie>) {
        savePopularMoviesCalls += 1
        lastSaved = movies
        cache.clear()
        cache.addAll(movies)
    }
}
