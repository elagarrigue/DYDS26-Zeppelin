package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.omdb.proxy.OMDBMoviesProxy
import edu.dyds.movies.data.external.tmdb.proxy.TMDBMoviesProxy
import edu.dyds.movies.data.FakeTMDBMoviesExternalSource
import edu.dyds.movies.data.FakeOMDBMoviesExternalSource
import edu.dyds.movies.data.external.omdbRemoteMovie
import edu.dyds.movies.data.external.tmdbRemoteMovie
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
        val tmdbMovie = movie(id = 1, overview = "Tmdb overview")
        val omdbMovie = movie(id = 2, overview = "Omdb overview")
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(
            movieDetailsResult = tmdbRemoteMovie(
                id = tmdbMovie.id,
                overview = tmdbMovie.overview,
                posterPath = null,
                backdropPath = null
            )
        )
        val omdbExternalSource = FakeOMDBMoviesExternalSource(
            result = omdbRemoteMovie(
                id = omdbMovie.id,
                overview = omdbMovie.overview,
                poster = ""
            )
        )
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movie(
            id = tmdbMovie.id,
            overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
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
    fun `getMovieByTitle should return TMDB result when OMDB is missing`() = runTest {
        // arrange
        val tmdbMovie = movie(id = 1, overview = "Tmdb overview")
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(
            movieDetailsResult = tmdbRemoteMovie(
                id = tmdbMovie.id,
                overview = tmdbMovie.overview,
                posterPath = null,
                backdropPath = null
            )
        )
        val omdbExternalSource = FakeOMDBMoviesExternalSource(exception = IllegalStateException())
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movie(
            id = tmdbMovie.id,
            overview = "TMDB: ${tmdbMovie.overview}",
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
        val omdbMovie = movie(id = 1, title = title, overview = "Omdb overview")
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsException = IllegalStateException())
        val omdbExternalSource = FakeOMDBMoviesExternalSource(
            result = omdbRemoteMovie(
                id = omdbMovie.id,
                title = title,
                overview = omdbMovie.overview
            )
        )
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = Movie(
            id = title.hashCode(),
            title = title,
            overview = "OMDB: ${omdbMovie.overview}",
            releaseDate = omdbMovie.releaseDate,
            poster = omdbMovie.poster,
            backdrop = omdbMovie.poster,
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
    fun `getMovieByTitle should return OMDB result when TMDB returns null`() = runTest {
        // arrange
        val title = "The Matrix"
        val omdbMovie = movie(id = 1, title = title, overview = "Omdb overview")
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = null)
        val omdbExternalSource = FakeOMDBMoviesExternalSource(
            result = omdbRemoteMovie(
                id = omdbMovie.id,
                title = title,
                overview = omdbMovie.overview
            )
        )
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = Movie(
            id = title.hashCode(),
            title = title,
            overview = "OMDB: ${omdbMovie.overview}",
            releaseDate = omdbMovie.releaseDate,
            poster = omdbMovie.poster,
            backdrop = omdbMovie.poster,
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
    fun `getMovieByTitle should return TMDB result when OMDB returns null`() = runTest {
        // arrange
        val tmdbMovie = movie(id = 1, overview = "Tmdb overview")
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(
            movieDetailsResult = tmdbRemoteMovie(
                id = tmdbMovie.id,
                overview = tmdbMovie.overview,
                posterPath = null,
                backdropPath = null
            )
        )
        val omdbExternalSource = FakeOMDBMoviesExternalSource(result = null)
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movie(
            id = tmdbMovie.id,
            overview = "TMDB: ${tmdbMovie.overview}",
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

