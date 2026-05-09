package edu.dyds.movies.data.local

import edu.dyds.movies.movie
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalMoviesDataSourceImplTest {

    @Test
    fun `getPopularMovies should return empty list when cache is empty`() {
        // arrange
        val dataSource = LocalMoviesDataSourceImpl()

        // act
        val result = dataSource.getPopularMovies()

        // assert
        assertEquals(emptyList(), result)
    }

    @Test
    fun `savePopularMovies should replace cached movies`() {
        // arrange
        val dataSource = LocalMoviesDataSourceImpl()
        val first = listOf(movie(id = 1), movie(id = 2))
        val second = listOf(movie(id = 3))

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
        val dataSource = LocalMoviesDataSourceImpl()
        val movies = mutableListOf(movie(id = 1))

        // act
        dataSource.savePopularMovies(movies)
        movies.add(movie(id = 2))
        val result = dataSource.getPopularMovies()

        // assert
        assertEquals(listOf(movie(id = 1)), result)
    }
}

