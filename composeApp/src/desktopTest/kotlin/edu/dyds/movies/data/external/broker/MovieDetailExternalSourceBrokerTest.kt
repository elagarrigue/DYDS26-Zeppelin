package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.FakeMovieDetailExternalSource
import edu.dyds.movies.movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieDetailExternalSourceBrokerTest {

    @Test
    fun `getMovieByTitle should merge TMDB and OMDB results when both are available`() = runTest {
        val tmdbMovie = movie(id = 1, overview = "Tmdb overview", popularity = 8.0, voteAverage = 6.0)
        val omdbMovie = movie(id = 2, overview = "Omdb overview", popularity = 4.0, voteAverage = 2.0)
        val tmdbSource = FakeMovieDetailExternalSource(result = tmdbMovie)
        val omdbSource = FakeMovieDetailExternalSource(result = omdbMovie)
        val broker = MovieDetailExternalSourceBroker(tmdbSource, omdbSource)

        val result = broker.getMovieByTitle("Any")

        val expected = movie(
            id = tmdbMovie.id,
            title = tmdbMovie.title,
            overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
            releaseDate = tmdbMovie.releaseDate,
            poster = tmdbMovie.poster,
            backdrop = tmdbMovie.backdrop,
            originalTitle = tmdbMovie.originalTitle,
            originalLanguage = tmdbMovie.originalLanguage,
            popularity = 6.0,
            voteAverage = 4.0
        )
        assertEquals(expected, result)
        assertEquals(1, tmdbSource.getMovieByTitleCalls)
        assertEquals(1, omdbSource.getMovieByTitleCalls)
    }

    @Test
    fun `getMovieByTitle should return TMDB result when OMDB is missing`() = runTest {
        val tmdbMovie = movie(id = 1, overview = "Tmdb overview")
        val tmdbSource = FakeMovieDetailExternalSource(result = tmdbMovie)
        val omdbSource = FakeMovieDetailExternalSource(result = null)
        val broker = MovieDetailExternalSourceBroker(tmdbSource, omdbSource)

        val result = broker.getMovieByTitle(tmdbMovie.title)

        val expected = tmdbMovie.copy(overview = "TMDB: ${tmdbMovie.overview}")
        assertEquals(expected, result)
        assertEquals(1, tmdbSource.getMovieByTitleCalls)
        assertEquals(1, omdbSource.getMovieByTitleCalls)
    }

    @Test
    fun `getMovieByTitle should return OMDB result when TMDB is missing`() = runTest {
        val omdbMovie = movie(id = 2, overview = "Omdb overview")
        val tmdbSource = FakeMovieDetailExternalSource(result = null)
        val omdbSource = FakeMovieDetailExternalSource(result = omdbMovie)
        val broker = MovieDetailExternalSourceBroker(tmdbSource, omdbSource)

        val result = broker.getMovieByTitle(omdbMovie.title)

        val expected = omdbMovie.copy(overview = "OMDB: ${omdbMovie.overview}")
        assertEquals(expected, result)
        assertEquals(1, tmdbSource.getMovieByTitleCalls)
        assertEquals(1, omdbSource.getMovieByTitleCalls)
    }

    @Test
    fun `getMovieByTitle should return null when both sources are missing`() = runTest {
        val tmdbSource = FakeMovieDetailExternalSource(result = null)
        val omdbSource = FakeMovieDetailExternalSource(result = null)
        val broker = MovieDetailExternalSourceBroker(tmdbSource, omdbSource)

        val result = broker.getMovieByTitle("Unknown")

        assertNull(result)
        assertEquals(1, tmdbSource.getMovieByTitleCalls)
        assertEquals(1, omdbSource.getMovieByTitleCalls)
    }
}

