package edu.dyds.movies.data

import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.data.external.MovieDetailsExternalSource
import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.data.external.omdb.OMDBRemoteMovie
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBRemoteResult
import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie

class FakePopularMoviesExternalSource(
    var result: List<Movie>? = null,
    var exception: Exception? = null,
) : PopularMoviesExternalSource {
    var getPopularMoviesCalls = 0

    override suspend fun getPopularMovies(): List<Movie> {
        getPopularMoviesCalls += 1
        exception?.let { throw it }
        return requireNotNull(result)
    }
}

class FakeMovieDetailsExternalSource(
    var result: Movie? = null,
    var exception: Exception? = null,
) : MovieDetailsExternalSource {
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

class FakeTMDBMoviesExternalSource(
    var popularResult: TMDBRemoteResult? = null,
    var detailResult: TMDBRemoteMovie? = null,
    var popularException: Exception? = null,
    var detailException: Exception? = null,
) : TMDBMoviesExternalSource {
    var getPopularMoviesCalls = 0
    var getMovieDetailsCalls = 0

    override suspend fun getMovie(title: String): TMDBRemoteMovie {
        getMovieDetailsCalls += 1
        detailException?.let { throw it }
        return requireNotNull(detailResult)
    }

    override suspend fun getPopularMovies(): TMDBRemoteResult {
        getPopularMoviesCalls += 1
        popularException?.let { throw it }
        return requireNotNull(popularResult)
    }
}

class FakeOMDBMoviesExternalSource(
    var result: OMDBRemoteMovie? = null,
    var exception: Exception? = null,
) : OMDBMoviesExternalSource {
    var getMovieDetailsCalls = 0

    override suspend fun getMovie(title: String): OMDBRemoteMovie {
        getMovieDetailsCalls += 1
        exception?.let { throw it }
        return requireNotNull(result)
    }
}
