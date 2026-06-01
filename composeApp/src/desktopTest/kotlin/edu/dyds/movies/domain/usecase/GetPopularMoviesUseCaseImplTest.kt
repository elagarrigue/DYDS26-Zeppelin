package edu.dyds.movies.domain.usecase

import edu.dyds.movies.movieFromSeed
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetPopularMoviesUseCaseImplTest {

    @Test
    fun `invoke should sort by vote average desc and classify movies`() = runTest {
        // arrange
        val low = movieFromSeed(seed = 1, voteAverage = 5.9)
        val edge = movieFromSeed(seed = 2, voteAverage = 6.0)
        val high = movieFromSeed(seed = 3, voteAverage = 8.2)
        val repository = FakeMoviesRepository(popularMovies = listOf(low, high, edge))
        val useCase = GetPopularMoviesUseCaseImpl(repository)

        // act
        val result = useCase()

        // assert
        assertEquals(listOf(high, edge, low), result.map { it.movie })
        assertEquals(3, result.size)
        assertEquals(1, repository.getPopularMoviesCalls)
        assertTrue(result.first { it.movie.id == 3 }.isGoodMovie)
        assertTrue(result.first { it.movie.id == 2 }.isGoodMovie)
        assertFalse(result.first { it.movie.id == 1 }.isGoodMovie)
    }

    @Test
    fun `invoke should return empty list when repository is empty`() = runTest {
        // arrange
        val repository = FakeMoviesRepository(popularMovies = emptyList())
        val useCase = GetPopularMoviesUseCaseImpl(repository)

        // act
        val result = useCase()

        // assert
        assertEquals(emptyList(), result)
        assertEquals(1, repository.getPopularMoviesCalls)
    }
}



