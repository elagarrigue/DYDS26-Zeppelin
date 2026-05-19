package edu.dyds.movies.data

import edu.dyds.movies.movie
import edu.dyds.movies.remoteMovie
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
        val remote = FakeRemoteMoviesExternalSource(popularMoviesException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, remote, local)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(localMovies, result)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(0, remote.getPopularMoviesCalls)
        assertEquals(0, local.savePopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should fetch remote data and cache when local is empty`() = runTest {
        // arrange
        val remoteMovies = listOf(movie(id = 1), movie(id = 2))
        val local = FakeMoviesLocalSource()
        val remote = FakeRemoteMoviesExternalSource(popularMoviesResult = remoteMovies)
        val repository = MoviesRepositoryImpl(remote, remote, local)
        val expected = remoteMovies

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(expected, result)
        assertEquals(1, remote.getPopularMoviesCalls)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(1, local.savePopularMoviesCalls)
        assertEquals(expected, local.lastSaved)
    }

    @Test
    fun `getPopularMovies should return empty list when remote fails`() = runTest {
        // arrange
        val local = FakeMoviesLocalSource()
        val remote = FakeRemoteMoviesExternalSource(popularMoviesException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, remote, local)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(emptyList(), result)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(1, remote.getPopularMoviesCalls)
        assertEquals(0, local.savePopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should cache empty list when remote returns empty`() = runTest {
        // arrange
        val local = FakeMoviesLocalSource()
        val remote = FakeRemoteMoviesExternalSource(popularMoviesResult = emptyList())
        val repository = MoviesRepositoryImpl(remote, remote, local)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(emptyList(), result)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(1, remote.getPopularMoviesCalls)
        assertEquals(1, local.savePopularMoviesCalls)
        assertEquals(emptyList(), local.lastSaved)
    }

    @Test
    fun `getMovieByTitle should return mapped movie when remote succeeds`() = runTest {
        // arrange
        val remoteMovie = remoteMovie(id = 20)
        val local = FakeMoviesLocalSource()
        val remote = FakeRemoteMoviesExternalSource(movieByTitleResult = remoteMovie.toDomainMovie())
        val repository = MoviesRepositoryImpl(remote, remote, local)
        val expected = remoteMovie.toDomainMovie()

        // act
        val result = repository.getMovieByTitle(remoteMovie.title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, remote.getMovieByTitleCalls)
    }

    @Test
    fun `getMovieByTitle should return null when remote fails`() = runTest {
        // arrange
        val local = FakeMoviesLocalSource()
        val remote = FakeRemoteMoviesExternalSource(movieByTitleException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, remote, local)

        // act
        val result = repository.getMovieByTitle("Unknown")

        // assert
        assertNull(result)
        assertEquals(1, remote.getMovieByTitleCalls)
    }

    @Test
    fun `getMovieDetails should return null when search has no results`() = runTest {
        // arrange
        val local = FakeMoviesLocalSource()
        val remote = FakeRemoteMoviesExternalSource(movieByTitleException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, remote, local)

        // act
        val result = repository.getMovieByTitle("Missing movie")

        // assert
        assertNull(result)
        assertEquals(1, remote.getMovieByTitleCalls)
    }
}
