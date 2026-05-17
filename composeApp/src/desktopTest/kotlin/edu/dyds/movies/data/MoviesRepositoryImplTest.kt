package edu.dyds.movies.data

import edu.dyds.movies.movie
import edu.dyds.movies.remoteMovie
import edu.dyds.movies.remoteResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MoviesRepositoryImplTest {

    @Test
    fun `getPopularMovies should return local data and skip remote when cache is not empty`() = runTest {
        // arrange
        val localMovies = listOf(movie(id = 1), movie(id = 2))
        val local = FakeLocalMoviesDataSource(initialMovies = localMovies)
        val remote = FakeRemoteMoviesDataSource(popularMoviesException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, local)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(localMovies, result)
        assertEquals(1, local.getPopularMoviesCalls)
        assertEquals(0, remote.getPopularMoviesCalls)
        assertEquals(0, local.savePopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should fetch remote data, map and cache when local is empty`() = runTest {
        // arrange
        val remoteMovies = listOf(remoteMovie(id = 1), remoteMovie(id = 2))
        val remoteResult = remoteResult(results = remoteMovies)
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(popularMoviesResult = remoteResult)
        val repository = MoviesRepositoryImpl(remote, local)
        val expected = remoteMovies.map { it.toDomainMovie() }

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
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(popularMoviesException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, local)

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
        val remoteResult = remoteResult(results = emptyList())
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(popularMoviesResult = remoteResult)
        val repository = MoviesRepositoryImpl(remote, local)

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
    fun `getMovieDetails should return mapped movie when remote succeeds`() = runTest {
        // arrange
        val remoteMovie = remoteMovie(id = 20)
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(movieDetailsResult = remoteMovie)
        val repository = MoviesRepositoryImpl(remote, local)
        val expected = remoteMovie.toDomainMovie()

        // act
        val result = repository.getMovieDetails(20)

        // assert
        assertEquals(expected, result)
        assertEquals(1, remote.getMovieDetailsCalls)
    }

    @Test
    fun `getMovieDetails should return null when remote fails`() = runTest {
        // arrange
        val local = FakeLocalMoviesDataSource()
        val remote = FakeRemoteMoviesDataSource(movieDetailsException = IllegalStateException())
        val repository = MoviesRepositoryImpl(remote, local)

        // act
        val result = repository.getMovieDetails(20)

        // assert
        assertNull(result)
        assertEquals(1, remote.getMovieDetailsCalls)
    }
}
