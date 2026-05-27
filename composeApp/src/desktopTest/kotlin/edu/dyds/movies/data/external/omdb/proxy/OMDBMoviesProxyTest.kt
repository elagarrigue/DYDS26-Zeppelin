package edu.dyds.movies.data.external.omdb.proxy

import edu.dyds.movies.data.FakeOMDBMoviesExternalSource
import edu.dyds.movies.movieFromSeedAsOmdb
import edu.dyds.movies.omdbRemoteMovie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OMDBMoviesProxyTest {

    @Test
    fun `getMovieByTitle should map OMDB fields to domain movie`() = runTest {
        // arrange
        val remoteMovie = omdbRemoteMovie(seed = 1)
        val externalSource = FakeOMDBMoviesExternalSource(result = remoteMovie)
        val proxy = OMDBMoviesProxy(externalSource)
        val expected = movieFromSeedAsOmdb(seed = 1)

        // act
        val result = proxy.getMovieByTitle(remoteMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should fallback when OMDB returns N-A values`() = runTest {
        // arrange
        val remoteMovie = omdbRemoteMovie(
            seed = 1,
            released = "N/A",
            year = "2024",
            metaScore = "N/A",
            imdbRating = "N/A"
        )

        val externalSource = FakeOMDBMoviesExternalSource(result = remoteMovie)
        val proxy = OMDBMoviesProxy(externalSource)

        val expected = movieFromSeedAsOmdb(
            seed = 1,
            releaseDate = "2024",
            popularity = 0.0,
            voteAverage = 0.0
        )

        // act
        val result = proxy.getMovieByTitle(remoteMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should fallback when OMDB returns empty strings`() = runTest {
        // arrange
        val remoteMovie = omdbRemoteMovie(
            seed = 1,
            released = "",
            year = "2024",
            metaScore = "",
            imdbRating = ""
        )
        val externalSource = FakeOMDBMoviesExternalSource(result = remoteMovie)
        val proxy = OMDBMoviesProxy(externalSource)

        val expected = movieFromSeedAsOmdb(
            seed = 1,
            releaseDate = "2024",
            popularity = 0.0,
            voteAverage = 0.0
        )

        // act
        val result = proxy.getMovieByTitle("Title")

        // assert
        assertEquals(expected, result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when external source fails`() = runTest {
        // arrange
        val externalSource = FakeOMDBMoviesExternalSource(exception = IllegalStateException())
        val proxy = OMDBMoviesProxy(externalSource)

        // act
        val result = proxy.getMovieByTitle("Missing")

        // assert
        assertNull(result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when external source returns null`() = runTest {
        // arrange
        val externalSource = FakeOMDBMoviesExternalSource(result = null)
        val proxy = OMDBMoviesProxy(externalSource)

        // act
        val result = proxy.getMovieByTitle("Missing")

        // assert
        assertNull(result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }
}
