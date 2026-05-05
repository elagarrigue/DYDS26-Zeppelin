package edu.dyds.movies.testdoubles

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteMoviesDataSource
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.data.local.LocalMoviesDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

fun movie(
    id: Int,
    title: String = "Movie $id",
    overview: String = "Overview $id",
    releaseDate: String = "2024-01-01",
    poster: String = "poster-$id",
    backdrop: String? = "backdrop-$id",
    originalTitle: String = "Original $id",
    originalLanguage: String = "en",
    popularity: Double = 10.0,
    voteAverage: Double = 5.0,
): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        poster = poster,
        backdrop = backdrop,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        popularity = popularity,
        voteAverage = voteAverage
    )
}

fun remoteMovie(
    id: Int,
    title: String = "Remote $id",
    overview: String = "Remote overview $id",
    releaseDate: String = "2024-01-01",
    posterPath: String = "/poster-$id.png",
    backdropPath: String? = "/backdrop-$id.png",
    originalTitle: String = "Remote original $id",
    originalLanguage: String = "en",
    popularity: Double = 10.0,
    voteAverage: Double = 5.0,
): RemoteMovie {
    return RemoteMovie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = posterPath,
        backdropPath = backdropPath,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        popularity = popularity,
        voteAverage = voteAverage
    )
}

fun remoteResult(
    page: Int = 1,
    results: List<RemoteMovie>,
    totalPages: Int = 1,
    totalResults: Int = results.size,
): RemoteResult {
    return RemoteResult(
        page = page,
        results = results,
        totalPages = totalPages,
        totalResults = totalResults
    )
}

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

class FakeRemoteMoviesDataSource(
    var popularMoviesResult: RemoteResult? = null,
    var movieDetailsResult: RemoteMovie? = null,
    var popularMoviesException: Exception? = null,
    var movieDetailsException: Exception? = null,
) : RemoteMoviesDataSource {
    var getPopularMoviesCalls = 0
    var getMovieDetailsCalls = 0

    override suspend fun getPopularMovies(): RemoteResult {
        getPopularMoviesCalls += 1
        popularMoviesException?.let { throw it }
        return requireNotNull(popularMoviesResult)
    }

    override suspend fun getMovieDetails(id: Int): RemoteMovie {
        getMovieDetailsCalls += 1
        movieDetailsException?.let { throw it }
        return requireNotNull(movieDetailsResult)
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

