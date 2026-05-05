package edu.dyds.movies.data

import edu.dyds.movies.testdoubles.FakeLocalMoviesDataSource
import edu.dyds.movies.testdoubles.FakeRemoteMoviesDataSource
import edu.dyds.movies.testdoubles.movie
import edu.dyds.movies.testdoubles.remoteMovie
import edu.dyds.movies.testdoubles.remoteResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MoviesRepositoryImplTest {

    @Test
    fun `getPopularMovies should return local data and skip remote when cache is not empty`() = runTest {
        val localMovies = listOf(movie(id = 1), movie(id = 2))
        val local = FakeLocalMoviesDataSource(initialMovies = localMovies)
        val remote = FakeRemoteMoviesDataSource(popularMoviesException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, local)

        val result = repository.getPopularMovies()

        assertEquals(localMovies, result)
        assertEquals(0, remote.getPopularMoviesCalls)
        assertEquals(0, local.savePopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should fetch remote data, map and cache when local is empty`() = runTest {
        val remoteMovies = listOf(remoteMovie(id = 1), remoteMovie(id = 2))
        val remoteResult = remoteResult(results = remoteMovies)
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(popularMoviesResult = remoteResult)
        val repository = MoviesRepositoryImpl(remote, local)

        val result = repository.getPopularMovies()
        val expected = remoteResult.toDomainMovieList()

        assertEquals(expected, result)
        assertEquals(1, remote.getPopularMoviesCalls)
        assertEquals(1, local.savePopularMoviesCalls)
        assertEquals(expected, local.lastSaved)
    }

    @Test
    fun `getPopularMovies should return empty list when remote fails`() = runTest {
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(popularMoviesException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, local)

        val result = repository.getPopularMovies()

        assertEquals(emptyList(), result)
        assertEquals(1, remote.getPopularMoviesCalls)
        assertEquals(0, local.savePopularMoviesCalls)
    }

    @Test
    fun `getMovieDetails should return mapped movie when remote succeeds`() = runTest {
        val remoteMovie = remoteMovie(id = 20)
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(movieDetailsResult = remoteMovie)
        val repository = MoviesRepositoryImpl(remote, local)

        val result = repository.getMovieDetails(20)

        assertEquals(remoteMovie.toDomainMovie(), result)
        assertEquals(1, remote.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieDetails should return null when remote fails`() = runTest {
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(movieDetailsException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, local)

        val result = repository.getMovieDetails(20)

        assertNull(result)
        assertEquals(1, remote.getMovieDetailsCalls)
    }
}

