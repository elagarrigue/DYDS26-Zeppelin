package edu.dyds.movies.data

import edu.dyds.movies.movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MoviesRepositoryImplTest {

    @Test
    fun `getPopularMovies should return local data and skip remote when cache is not empty`() = runTest {
        // arrange
        val localMovies = listOf(movie(id = 1), movie(id = 2))
        val local = FakeMoviesLocalSource(initialMovies = localMovies)
        val popularRemote = FakePopularMoviesExternalSource(exception = IllegalStateException())
        val movieDetailsExternalSource = FakeMovieDetailsExternalSource()
        val repository = MoviesRepositoryImpl(popularRemote, movieDetailsExternalSource, local)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(localMovies, result)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(0, popularRemote.getPopularMoviesCalls)
        assertEquals(0, local.savePopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should fetch remote data and cache when local is empty`() = runTest {
        // arrange
        val expectedMovies = listOf(movie(id = 1), movie(id = 2))
        val local = FakeMoviesLocalSource()
        val popularRemote = FakePopularMoviesExternalSource(result = expectedMovies)
        val movieDetailsExternalSource = FakeMovieDetailsExternalSource()
        val repository = MoviesRepositoryImpl(popularRemote, movieDetailsExternalSource, local)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(expectedMovies, result)
        assertEquals(1, popularRemote.getPopularMoviesCalls)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(1, local.savePopularMoviesCalls)
        assertEquals(expectedMovies, local.lastSaved)
    }

    @Test
    fun `getPopularMovies should return empty list when remote fails`() = runTest {
        // arrange
        val local = FakeMoviesLocalSource()
        val popularRemote = FakePopularMoviesExternalSource(exception = IllegalStateException())
        val movieDetailsExternalSource = FakeMovieDetailsExternalSource()
        val repository = MoviesRepositoryImpl(popularRemote, movieDetailsExternalSource, local)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(emptyList(), result)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(1, popularRemote.getPopularMoviesCalls)
        assertEquals(0, local.savePopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should cache empty list when remote returns empty`() = runTest {
        // arrange
        val local = FakeMoviesLocalSource()
        val popularRemote = FakePopularMoviesExternalSource(result = emptyList())
        val movieDetailsExternalSource = FakeMovieDetailsExternalSource()
        val repository = MoviesRepositoryImpl(popularRemote, movieDetailsExternalSource, local)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(emptyList(), result)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(1, popularRemote.getPopularMoviesCalls)
        assertEquals(1, local.savePopularMoviesCalls)
        assertEquals(emptyList(), local.lastSaved)
    }

    @Test
    fun `getMovieByTitle should return mapped movie when remote succeeds`() = runTest {
        // arrange
        val expectedMovie = movie(id = 20)
        val local = FakeMoviesLocalSource()
        val popularRemote = FakePopularMoviesExternalSource()
        val movieDetailsExternalSource = FakeMovieDetailsExternalSource(result = expectedMovie)
        val repository = MoviesRepositoryImpl(popularRemote, movieDetailsExternalSource, local)

        // act
        val result = repository.getMovieByTitle(expectedMovie.title)

        // assert
        assertEquals(expectedMovie, result)
        assertEquals(1, movieDetailsExternalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when remote fails`() = runTest {
        // arrange
        val local = FakeMoviesLocalSource()
        val popularRemote = FakePopularMoviesExternalSource()
        val movieDetailsExternalSource = FakeMovieDetailsExternalSource(exception = IllegalStateException())
        val repository = MoviesRepositoryImpl(popularRemote, movieDetailsExternalSource, local)

        // act
        val result = repository.getMovieByTitle("Unknown")

        // assert
        assertNull(result)
        assertEquals(1, movieDetailsExternalSource.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieByTitle should return null when search has no results`() = runTest {
        // arrange
        val local = FakeMoviesLocalSource()
        val popularRemote = FakePopularMoviesExternalSource()
        val movieDetailsExternalSource = FakeMovieDetailsExternalSource(exception = IllegalStateException())
        val repository = MoviesRepositoryImpl(popularRemote, movieDetailsExternalSource, local)

        // act
        val result = repository.getMovieByTitle("Missing movie")

        // assert
        assertNull(result)
        assertEquals(1, movieDetailsExternalSource.getMovieDetailsCalls)
    }
}
