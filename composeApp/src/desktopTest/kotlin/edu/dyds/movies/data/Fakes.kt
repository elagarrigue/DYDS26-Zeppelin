package edu.dyds.movies.data

import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie
import edu.dyds.movies.data.external.PopularMoviesDataSource
import edu.dyds.movies.data.external.MovieDetailDataSource
import edu.dyds.movies.data.external.tmdb.TMDBRemoteResult
import edu.dyds.movies.data.local.LocalMoviesDataSource
import edu.dyds.movies.domain.entity.Movie

class FakeRemoteMoviesDataSource(
    var popularMoviesResult: TMDBRemoteResult? = null,
    var movieByTitleResult: TMDBRemoteMovie? = null,
    var popularMoviesException: Exception? = null,
    var movieByTitleException: Exception? = null,
) : PopularMoviesDataSource, MovieDetailDataSource {
    var getPopularMoviesCalls = 0
    var getMovieByTitleCalls = 0

    override suspend fun getPopularMovies(): TMDBRemoteResult {
        getPopularMoviesCalls += 1
        popularMoviesException?.let { throw it }
        return requireNotNull(popularMoviesResult)
    }

    override suspend fun getMovieByTitle(title: String): TMDBRemoteMovie {
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
