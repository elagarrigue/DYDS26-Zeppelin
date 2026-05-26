package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.omdb.proxy.OMDBMoviesProxy
import edu.dyds.movies.data.external.tmdb.proxy.TMDBMoviesProxy
import edu.dyds.movies.data.FakeTMDBMoviesExternalSource
import edu.dyds.movies.data.FakeOMDBMoviesExternalSource
import edu.dyds.movies.movie
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieDetailsExternalSourceBrokerTest {

    @Test
    fun `getMovieByTitle should merge TMDB and OMDB results when both are available`() = runTest {
        // arrange
        val tmdbMovie = movie(id = 1, overview = "Tmdb overview", popularity = 8.0, voteAverage = 6.0)
        val omdbMovie = movie(id = 2, overview = "Omdb overview", popularity = 4.0, voteAverage = 2.0)
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = remoteMovie(tmdbMovie))
        val omdbExternalSource = FakeOMDBMoviesExternalSource(result = remoteOmdbMovie(omdbMovie))
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movie(
            id = tmdbMovie.id,
            title = tmdbMovie.title,
            overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
            releaseDate = tmdbMovie.releaseDate,
            poster = "",
            backdrop = null,
            originalTitle = tmdbMovie.originalTitle,
            originalLanguage = tmdbMovie.originalLanguage,
            popularity = 6.0,
            voteAverage = 4.0
        )

        // act
        val result = broker.getMovieByTitle("Any")

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbExternalSource.getMovieDetailsCalls)
        assertEquals(1, omdbExternalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return TMDB result when OMDB is missing`() = runTest {
        // arrange
        val tmdbMovie = movie(id = 1, overview = "Tmdb overview")
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = remoteMovie(tmdbMovie))
        val omdbExternalSource = FakeOMDBMoviesExternalSource(exception = IllegalStateException())
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movie(
            id = tmdbMovie.id,
            overview = "TMDB: ${tmdbMovie.overview}",
            originalTitle = tmdbMovie.originalTitle,
            originalLanguage = tmdbMovie.originalLanguage,
            poster = "",
            backdrop = null
        )

        // act
        val result = broker.getMovieByTitle(tmdbMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbExternalSource.getMovieDetailsCalls)
        assertEquals(1, omdbExternalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return OMDB result when TMDB is missing`() = runTest {
        // arrange
        val title = "The Matrix"
        val posterUrl = "http://example.com/poster.jpg"
        val omdbMovie = movie(
            id = 1,
            title = title,
            overview = "Omdb overview",
            releaseDate = "1999-03-31",
            poster = posterUrl
        )
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsException = IllegalStateException())
        val omdbExternalSource = FakeOMDBMoviesExternalSource(result = remoteOmdbMovie(omdbMovie, posterUrl))
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = Movie(
            id = title.hashCode(),
            title = title,
            overview = "OMDB: ${omdbMovie.overview}",
            releaseDate = omdbMovie.releaseDate,
            poster = posterUrl,
            backdrop = posterUrl,
            originalTitle = title,
            originalLanguage = omdbMovie.originalLanguage,
            popularity = omdbMovie.popularity,
            voteAverage = omdbMovie.voteAverage
        )

        // act
        val result = broker.getMovieByTitle(title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbExternalSource.getMovieDetailsCalls)
        assertEquals(1, omdbExternalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when both sources are missing`() = runTest {
        // arrange
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsException = IllegalStateException())
        val omdbExternalSource = FakeOMDBMoviesExternalSource(exception = IllegalStateException())
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)

        // act
        val result = broker.getMovieByTitle("Unknown")

        // assert
        assertNull(result)
        assertEquals(1, tmdbExternalSource.getMovieDetailsCalls)
        assertEquals(1, omdbExternalSource.getMovieDetailsCalls)
    }
}

