package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie

class FakeMoviesExternalSource(
    var result: List<Movie>? = null,
    var exception: Exception? = null,
) : MoviesExternalSource {
    var getPopularMoviesCalls = 0

    override suspend fun getPopularMovies(): List<Movie> {
        getPopularMoviesCalls += 1
        exception?.let { throw it }
        return requireNotNull(result)
    }
}

class FakeMovieExternalSource(
    var result: Movie? = null,
    var exception: Exception? = null,
) : MovieExternalSource {
    var getMovieByTitleCalls = 0

    override suspend fun getMovieByTitle(title: String): Movie? {
        getMovieByTitleCalls += 1
        return runCatching {
            exception?.let { throw it }
            result
        }.getOrNull()
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
