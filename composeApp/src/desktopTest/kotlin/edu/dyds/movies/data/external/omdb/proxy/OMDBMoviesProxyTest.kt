package edu.dyds.movies.data.external.omdb.proxy

import edu.dyds.movies.data.FakeOMDBMoviesExternalSource
import edu.dyds.movies.FakeMovieDefaults
import edu.dyds.movies.data.external.omdbRemoteMovie
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OMDBMoviesProxyTest {

    @Test
    fun `getMovieByTitle should map OMDB fields to domain movie`() = runTest {
        // arrange
        val title = FakeMovieDefaults.title(1)
        val overview = FakeMovieDefaults.overview(1)
        val poster = FakeMovieDefaults.poster(1)
        val remoteMovie = omdbRemoteMovie(
            id = 1,
            released = "2020-01-01",
            year = "2019",
            metaScore = "70",
            imdbRating = "8.5"
        )
        val externalSource = FakeOMDBMoviesExternalSource(result = remoteMovie)
        val proxy = OMDBMoviesProxy(externalSource)
        val expected = Movie(
            id = title.hashCode(),
            title = title,
            overview = overview,
            releaseDate = "2020-01-01",
            poster = poster,
            backdrop = poster,
            originalTitle = title,
            originalLanguage = "en",
            popularity = 8.5,
            voteAverage = 70.0
        )

        // act
        val result = proxy.getMovieByTitle("Title")

        // assert
        assertEquals(expected, result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should fallback when OMDB returns N-A values`() = runTest {
        // arrange
        val title = FakeMovieDefaults.title(1)
        val overview = FakeMovieDefaults.overview(1)
        val poster = FakeMovieDefaults.poster(1)
        val remoteMovie = omdbRemoteMovie(
            id = 1,
            released = "N/A",
            year = "2018",
            metaScore = "N/A",
            imdbRating = "N/A"
        )
        val externalSource = FakeOMDBMoviesExternalSource(result = remoteMovie)
        val proxy = OMDBMoviesProxy(externalSource)
        val expected = Movie(
            id = title.hashCode(),
            title = title,
            overview = overview,
            releaseDate = "2018",
            poster = poster,
            backdrop = poster,
            originalTitle = title,
            originalLanguage = "en",
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
    fun `getMovieByTitle should fallback when OMDB returns empty strings`() = runTest {
        // arrange
        val title = FakeMovieDefaults.title(1)
        val overview = FakeMovieDefaults.overview(1)
        val poster = FakeMovieDefaults.poster(1)
        val remoteMovie = omdbRemoteMovie(
            id = 1,
            released = "",
            year = "2017",
            metaScore = "",
            imdbRating = ""
        )
        val externalSource = FakeOMDBMoviesExternalSource(result = remoteMovie)
        val proxy = OMDBMoviesProxy(externalSource)
        val expected = Movie(
            id = title.hashCode(),
            title = title,
            overview = overview,
            releaseDate = "2017",
            poster = poster,
            backdrop = poster,
            originalTitle = title,
            originalLanguage = "en",
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
