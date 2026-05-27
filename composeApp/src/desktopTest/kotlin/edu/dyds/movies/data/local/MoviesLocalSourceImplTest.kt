package edu.dyds.movies.data.local

import edu.dyds.movies.movieFromSeed
import kotlin.test.Test
import kotlin.test.assertEquals

class MoviesLocalSourceImplTest {

    @Test
    fun `getPopularMovies should return empty list when cache is empty`() {
        // arrange
        val dataSource = MoviesLocalSourceImpl()

        // act
        val result = dataSource.getPopularMovies()

        // assert
        assertEquals(emptyList(), result)
    }

    @Test
    fun `savePopularMovies should replace cached movies`() {
        // arrange
        val dataSource = MoviesLocalSourceImpl()
        val first = listOf(movieFromSeed(seed = 1), movieFromSeed(seed = 2))
        val second = listOf(movieFromSeed(seed = 3))

        // act
        dataSource.savePopularMovies(first)
        dataSource.savePopularMovies(second)
        val result = dataSource.getPopularMovies()

        // assert
        assertEquals(second, result)
    }

    @Test
    fun `savePopularMovies should not be affected by external list mutation`() {
        // arrange
        val dataSource = MoviesLocalSourceImpl()
        val movies = mutableListOf(movieFromSeed(seed = 1))

        // act
        dataSource.savePopularMovies(movies)
        movies.add(movieFromSeed(seed = 2))
        val result = dataSource.getPopularMovies()

        // assert
        assertEquals(listOf(movieFromSeed(seed = 1)), result)
    }

    @Test
    fun `getPopularMovies returned list mutation should not affect internal cache`() {
        // arrange
        val dataSource = MoviesLocalSourceImpl()
        val original = listOf(movieFromSeed(seed = 1))
        dataSource.savePopularMovies(original)

        // act
        val result = dataSource.getPopularMovies()
        val mutableCopy = result.toMutableList()
        mutableCopy.add(movieFromSeed(seed = 2))
        val afterMutation = dataSource.getPopularMovies()

        // assert
        assertEquals(original, afterMutation)
    }
}

