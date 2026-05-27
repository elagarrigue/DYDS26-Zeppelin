package edu.dyds.movies.data.external.tmdb.proxy

import edu.dyds.movies.data.FakeTMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdbRemoteMovie
import edu.dyds.movies.data.external.tmdbRemoteResult
import edu.dyds.movies.movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TMDBMoviesProxyTest {

    @Test
    fun `getPopularMovies should map results to domain movies`() = runTest {
        // arrange
        val remoteMovie = tmdbRemoteMovie(
            id = 10,
            posterPath = "/poster.png",
            backdropPath = "/backdrop.png"
        )
        val externalSource = FakeTMDBMoviesExternalSource(
            popularResult = tmdbRemoteResult(results = listOf(remoteMovie))
        )
        val proxy = TMDBMoviesProxy(externalSource)
        val expected = movie(
            id = 10,
            poster = "https://image.tmdb.org/t/p/w185/poster.png",
            backdrop = "https://image.tmdb.org/t/p/w780/backdrop.png"
        )

        // act
        val result = proxy.getPopularMovies()

        // assert
        assertEquals(listOf(expected), result)
        assertEquals(1, externalSource.getPopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should return empty list when external source returns no results`() = runTest {
        // arrange
        val externalSource = FakeTMDBMoviesExternalSource(
            popularResult = tmdbRemoteResult(results = emptyList(), totalResults = 0)
        )
        val proxy = TMDBMoviesProxy(externalSource)

        // act
        val result = proxy.getPopularMovies()

        // assert
        assertEquals(emptyList(), result)
        assertEquals(1, externalSource.getPopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should return empty list when external source fails`() = runTest {
        // arrange
        val externalSource = FakeTMDBMoviesExternalSource(popularException = IllegalStateException())
        val proxy = TMDBMoviesProxy(externalSource)

        // act
        val result = proxy.getPopularMovies()

        // assert
        assertEquals(emptyList(), result)
        assertEquals(1, externalSource.getPopularMoviesCalls)
    }

    @Test
    fun `getMovieByTitle should map the first result`() = runTest {
        // arrange
        val remoteMovie = tmdbRemoteMovie(
            id = 20,
            releaseDate = null,
            posterPath = null,
            backdropPath = null,
            popularity = null,
            voteAverage = null
        )
        val externalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = remoteMovie)
        val proxy = TMDBMoviesProxy(externalSource)
        val expected = movie(
            id = 20,
            releaseDate = "",
            poster = "",
            backdrop = null,
            popularity = 0.0,
            voteAverage = 0.0
        )

        // act
        val result = proxy.getMovieByTitle("Detail")

        // assert
        assertEquals(expected, result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should map empty poster and backdrop paths to empty strings`() = runTest {
        // arrange
        val remoteMovie = tmdbRemoteMovie(
            id = 30,
            posterPath = "",
            backdropPath = ""
        )
        val externalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = remoteMovie)
        val proxy = TMDBMoviesProxy(externalSource)
        val expected = movie(
            id = 30,
            poster = "https://image.tmdb.org/t/p/w185",
            backdrop = "https://image.tmdb.org/t/p/w780"
        )

        // act
        val result = proxy.getMovieByTitle("Detail")

        // assert
        assertEquals(expected, result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when external source fails`() = runTest {
        // arrange
        val externalSource = FakeTMDBMoviesExternalSource(movieDetailsException = IllegalStateException())
        val proxy = TMDBMoviesProxy(externalSource)

        // act
        val result = proxy.getMovieByTitle("Missing")

        // assert
        assertNull(result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when external source returns null`() = runTest {
        // arrange
        val externalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = null)
        val proxy = TMDBMoviesProxy(externalSource)

        // act
        val result = proxy.getMovieByTitle("Missing")

        // assert
        assertNull(result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }
}
