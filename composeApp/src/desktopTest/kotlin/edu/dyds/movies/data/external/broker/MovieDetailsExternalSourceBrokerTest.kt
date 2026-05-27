package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.omdb.proxy.OMDBMoviesProxy
import edu.dyds.movies.data.external.tmdb.proxy.TMDBMoviesProxy
import edu.dyds.movies.data.FakeTMDBMoviesExternalSource
import edu.dyds.movies.data.FakeOMDBMoviesExternalSource
import edu.dyds.movies.movieFromSeed
import edu.dyds.movies.movieFromSeedAsOmdb
import edu.dyds.movies.omdbRemoteMovie
import edu.dyds.movies.tmdbRemoteMovie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieDetailsExternalSourceBrokerTest {

    @Test
    fun `getMovieByTitle should merge TMDB and OMDB results when both are available`() = runTest {
        // arrange
        val tmdbMovie = tmdbRemoteMovie(seed = 1, popularity = 8.0, voteAverage = 6.0)
        val omdbMovie = omdbRemoteMovie(seed = 2, imdbRating = "4.0", metaScore = "2.0")
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = tmdbMovie)
        val omdbExternalSource = FakeOMDBMoviesExternalSource(result = omdbMovie)
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movieFromSeed(
            seed = tmdbMovie.id,
            overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.plot}",
            poster = "https://image.tmdb.org/t/p/w185${tmdbMovie.posterPath}",
            backdrop = "https://image.tmdb.org/t/p/w780${tmdbMovie.backdropPath}",
            popularity = 6.0,
            voteAverage = 4.0
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
        val tmdbMovie = tmdbRemoteMovie(seed = 1)
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = tmdbMovie)
        val omdbExternalSource = FakeOMDBMoviesExternalSource(exception = IllegalStateException())
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movieFromSeed(
            seed = tmdbMovie.id,
            overview = "TMDB: ${tmdbMovie.overview}",
            poster = "https://image.tmdb.org/t/p/w185${tmdbMovie.posterPath}",
            backdrop = "https://image.tmdb.org/t/p/w780${tmdbMovie.backdropPath}",
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
        val omdbMovie = omdbRemoteMovie(seed = 1)
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsException = IllegalStateException())
        val omdbExternalSource = FakeOMDBMoviesExternalSource(result = omdbMovie)
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movieFromSeedAsOmdb(
            seed = 1,
            overview = "OMDB: ${omdbMovie.plot}",
        )

        // act
        val result = broker.getMovieByTitle(omdbMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbExternalSource.getMovieDetailsCalls)
        assertEquals(1, omdbExternalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return OMDB result when TMDB returns null`() = runTest {
        // arrange
        val omdbMovie = omdbRemoteMovie(seed = 1)
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = null)
        val omdbExternalSource = FakeOMDBMoviesExternalSource(result = omdbMovie)
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movieFromSeedAsOmdb(
            seed = 1,
            overview = "OMDB: ${omdbMovie.plot}",
        )

        // act
        val result = broker.getMovieByTitle(omdbMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, tmdbExternalSource.getMovieDetailsCalls)
        assertEquals(1, omdbExternalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return TMDB result when OMDB returns null`() = runTest {
        // arrange
        val tmdbMovie = tmdbRemoteMovie(seed = 1)
        val tmdbExternalSource = FakeTMDBMoviesExternalSource(
            movieDetailsResult = tmdbMovie
        )
        val omdbExternalSource = FakeOMDBMoviesExternalSource(result = null)
        val tmdbProxy = TMDBMoviesProxy(tmdbExternalSource)
        val omdbProxy = OMDBMoviesProxy(omdbExternalSource)
        val broker = MovieDetailsExternalSourceBroker(tmdbProxy, omdbProxy)
        val expected = movieFromSeed(
            seed = 1,
            overview = "TMDB: ${tmdbMovie.overview}",
            poster = "https://image.tmdb.org/t/p/w185${tmdbMovie.posterPath}",
            backdrop = "https://image.tmdb.org/t/p/w780${tmdbMovie.backdropPath}",
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

