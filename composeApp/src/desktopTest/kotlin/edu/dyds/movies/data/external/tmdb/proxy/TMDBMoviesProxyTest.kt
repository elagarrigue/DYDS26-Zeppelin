package edu.dyds.movies.data.external.tmdb.proxy

import edu.dyds.movies.data.FakeTMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie
import edu.dyds.movies.data.external.tmdb.TMDBRemoteResult
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TMDBMoviesProxyTest {

    @Test
    fun `getPopularMovies should map results to domain movies`() = runTest {
        // arrange
        val remoteMovie = TMDBRemoteMovie(
            id = 10,
            title = "Title",
            overview = "Overview",
            releaseDate = "2024-01-01",
            posterPath = "/poster.png",
            backdropPath = "/backdrop.png",
            originalTitle = "Original",
            originalLanguage = "en",
            popularity = 7.0,
            voteAverage = 8.0
        )
        val externalSource = FakeTMDBMoviesExternalSource(
            popularResult = TMDBRemoteResult(
                page = 1,
                results = listOf(remoteMovie),
                totalPages = 1,
                totalResults = 1
            )
        )
        val proxy = TMDBMoviesProxy(externalSource)
        val expected = Movie(
            id = 10,
            title = "Title",
            overview = "Overview",
            releaseDate = "2024-01-01",
            poster = "https://image.tmdb.org/t/p/w185/poster.png",
            backdrop = "https://image.tmdb.org/t/p/w780/backdrop.png",
            originalTitle = "Original",
            originalLanguage = "en",
            popularity = 7.0,
            voteAverage = 8.0
        )

        // act
        val result = proxy.getPopularMovies()

        // assert
        assertEquals(listOf(expected), result)
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
    fun `getPopularMovies should return empty list when external source returns no results`() = runTest {
        // arrange
        val externalSource = FakeTMDBMoviesExternalSource(
            popularResult = TMDBRemoteResult(
                page = 1,
                results = emptyList(),
                totalPages = 1,
                totalResults = 0
            )
        )
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
        val remoteMovie = TMDBRemoteMovie(
            id = 20,
            title = "Detail",
            overview = "Detail overview",
            releaseDate = null,
            posterPath = null,
            backdropPath = null,
            originalTitle = "Detail original",
            originalLanguage = "es",
            popularity = null,
            voteAverage = null
        )
        val externalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = remoteMovie)
        val proxy = TMDBMoviesProxy(externalSource)
        val expected = Movie(
            id = 20,
            title = "Detail",
            overview = "Detail overview",
            releaseDate = "",
            poster = "",
            backdrop = null,
            originalTitle = "Detail original",
            originalLanguage = "es",
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

    @Test
    fun `getMovieByTitle should map empty poster and backdrop paths to empty strings`() = runTest {
        // arrange
        val remoteMovie = TMDBRemoteMovie(
            id = 30,
            title = "Detail",
            overview = "Detail overview",
            releaseDate = "2024-01-01",
            posterPath = "",
            backdropPath = "",
            originalTitle = "Detail original",
            originalLanguage = "en",
            popularity = 1.0,
            voteAverage = 2.0
        )
        val externalSource = FakeTMDBMoviesExternalSource(movieDetailsResult = remoteMovie)
        val proxy = TMDBMoviesProxy(externalSource)
        val expected = Movie(
            id = 30,
            title = "Detail",
            overview = "Detail overview",
            releaseDate = "2024-01-01",
            poster = "https://image.tmdb.org/t/p/w185",
            backdrop = "https://image.tmdb.org/t/p/w780",
            originalTitle = "Detail original",
            originalLanguage = "en",
            popularity = 1.0,
            voteAverage = 2.0
        )

        // act
        val result = proxy.getMovieByTitle("Detail")

        // assert
        assertEquals(expected, result)
        assertEquals(1, externalSource.getMovieDetailsCalls)
    }
}

