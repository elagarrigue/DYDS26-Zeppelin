package edu.dyds.movies.data.external.tmdb.proxy

import edu.dyds.movies.data.FakeTMDBMoviesExternalSource
import edu.dyds.movies.movieFromSeed
import edu.dyds.movies.tmdbRemoteMovie
import edu.dyds.movies.tmdbRemoteResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TMDBMoviesProxyTest {

    @Test
    fun `getPopularMovies should map results to domain movies`() = runTest {
        // arrange
        val remoteMovie = tmdbRemoteMovie(seed = 1)

        val externalSource = FakeTMDBMoviesExternalSource(tmdbRemoteResult(listOf(remoteMovie)))
        val proxy = TMDBMoviesProxy(externalSource)
        val expected = movieFromSeed(
            seed = 1,
            poster = "https://image.tmdb.org/t/p/w185${remoteMovie.posterPath}",
            backdrop = "https://image.tmdb.org/t/p/w780${remoteMovie.backdropPath}",
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
            popularResult = tmdbRemoteResult(results = emptyList())
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
    fun `getMovieByTitle should map the remote movie to a domain movie`() = runTest {
        // arrange
        val remoteMovie = tmdbRemoteMovie(seed = 1)
        val externalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = remoteMovie)
        val proxy = TMDBMoviesProxy(externalSource)
        val expected = movieFromSeed(
            seed = 1,
            poster = "https://image.tmdb.org/t/p/w185${remoteMovie.posterPath}",
            backdrop = "https://image.tmdb.org/t/p/w780${remoteMovie.backdropPath}"
        )

        // act
        val result = proxy.getMovieByTitle(remoteMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when external source returns empty results`() = runTest {
        // arrange
        val externalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = null)
        val proxy = TMDBMoviesProxy(externalSource)

        // act
        val result = proxy.getMovieByTitle("Missing")

        // assert
        assertNull(result)
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
}
