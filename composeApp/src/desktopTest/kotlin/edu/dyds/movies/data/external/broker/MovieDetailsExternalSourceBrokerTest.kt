package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.FakeOMDBMoviesProxy
import edu.dyds.movies.data.FakeTMDBMoviesProxy
import edu.dyds.movies.movieFromSeed
import edu.dyds.movies.movieFromSeedAsOmdb
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieDetailsExternalSourceBrokerTest {

    @Test
    fun `getMovieByTitle should merge TMDB and OMDB results when both are available`() = runTest {
        // arrange
        val tmdbMovie = movieFromSeed(seed = 1, popularity = 8.0, voteAverage = 6.0)
        val omdbMovie = movieFromSeedAsOmdb(seed = 2, popularity = 4.0, voteAverage = 2.0)
        val tmdbProxy = FakeTMDBMoviesProxy(result = tmdbMovie)
        val omdbProxy = FakeOMDBMoviesProxy(result = omdbMovie)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movieFromSeed(
            seed = 1,
            overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
            popularity = 6.0,
            voteAverage = 4.0
        )

        // act
        val result = broker.getMovieByTitle(tmdbMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbProxy.getMovieDetailsCalls)
        assertEquals(1, omdbProxy.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return TMDB result when OMDB is missing`() = runTest {
        // arrange
        val tmdbMovie = movieFromSeed(seed = 1)
        val tmdbProxy = FakeTMDBMoviesProxy(result = tmdbMovie)
        val omdbProxy = FakeOMDBMoviesProxy(exception = IllegalStateException())
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movieFromSeed(
            seed = 1,
            overview = "TMDB: ${tmdbMovie.overview}",
        )

        // act
        val result = broker.getMovieByTitle(tmdbMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbProxy.getMovieDetailsCalls)
        assertEquals(1, omdbProxy.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return OMDB result when TMDB is missing`() = runTest {
        // arrange
        val omdbMovie = movieFromSeedAsOmdb(seed = 1)
        val tmdbProxy = FakeTMDBMoviesProxy(exception = IllegalStateException())
        val omdbProxy = FakeOMDBMoviesProxy(result = omdbMovie)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = omdbMovie.copy(
            overview = "OMDB: ${omdbMovie.overview}",
        )

        // act
        val result = broker.getMovieByTitle(omdbMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbProxy.getMovieDetailsCalls)
        assertEquals(1, omdbProxy.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return OMDB result when TMDB returns null`() = runTest {
        // arrange
        val omdbMovie = movieFromSeedAsOmdb(seed = 1)
        val tmdbProxy = FakeTMDBMoviesProxy(result = null)
        val omdbProxy = FakeOMDBMoviesProxy(result = omdbMovie)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = omdbMovie.copy(
            overview = "OMDB: ${omdbMovie.overview}",
        )

        // act
        val result = broker.getMovieByTitle(omdbMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbProxy.getMovieDetailsCalls)
        assertEquals(1, omdbProxy.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return TMDB result when OMDB returns null`() = runTest {
        // arrange
        val tmdbMovie = movieFromSeed(seed = 1)
        val tmdbProxy = FakeTMDBMoviesProxy(
            result = tmdbMovie
        )
        val omdbProxy = FakeOMDBMoviesProxy(result = null)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movieFromSeed(
            seed = 1,
            overview = "TMDB: ${tmdbMovie.overview}",
        )

        // act
        val result = broker.getMovieByTitle(tmdbMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbProxy.getMovieDetailsCalls)
        assertEquals(1, omdbProxy.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when both sources are missing`() = runTest {
        // arrange
        val tmdbProxy = FakeTMDBMoviesProxy(exception = IllegalStateException())
        val omdbProxy = FakeOMDBMoviesProxy(exception = IllegalStateException())
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)

        // act
        val result = broker.getMovieByTitle("Unknown")

        // assert
        assertNull(result)
        assertEquals(1, tmdbProxy.getMovieDetailsCalls)
        assertEquals(1, omdbProxy.getMovieDetailsCalls)
    }
}
