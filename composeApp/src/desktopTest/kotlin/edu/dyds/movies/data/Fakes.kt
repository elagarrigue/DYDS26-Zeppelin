package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteMoviesDataSource
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.data.local.LocalMoviesDataSource
import edu.dyds.movies.domain.entity.Movie

class FakeRemoteMoviesDataSource(
    var popularMoviesResult: RemoteResult? = null,
    var movieByTitleResult: RemoteMovie? = null,
    var popularMoviesException: Exception? = null,
    var movieByTitleException: Exception? = null,
) : RemoteMoviesDataSource {
    var getPopularMoviesCalls = 0
    var getMovieByTitleCalls = 0

    override suspend fun getPopularMovies(): RemoteResult {
        getPopularMoviesCalls += 1
        popularMoviesException?.let { throw it }
        return requireNotNull(popularMoviesResult)
    }

    override suspend fun getMovieByTitle(title: String): RemoteMovie {
        getMovieByTitleCalls += 1
        movieByTitleException?.let { throw it }
        return requireNotNull(movieByTitleResult)
    }
}

class FakeLocalMoviesDataSource(
    initialMovies: List<Movie> = emptyList(),
) : LocalMoviesDataSource {
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
