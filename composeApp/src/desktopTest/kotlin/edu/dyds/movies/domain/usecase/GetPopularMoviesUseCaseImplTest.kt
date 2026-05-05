package edu.dyds.movies.domain.usecase

import edu.dyds.movies.testdoubles.FakeMoviesRepository
import edu.dyds.movies.testdoubles.movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetPopularMoviesUseCaseImplTest {

    @Test
    fun `invoke should sort by vote average desc and classify movies`() = runTest {
        val low = movie(id = 1, voteAverage = 5.9)
        val edge = movie(id = 2, voteAverage = 6.0)
        val high = movie(id = 3, voteAverage = 8.2)
        val repository = FakeMoviesRepository(popularMovies = listOf(low, high, edge))
        val useCase = GetPopularMoviesUseCaseImpl(repository)

        val result = useCase()

        assertEquals(listOf(high, edge, low), result.map { it.movie })
        assertTrue(result.first { it.movie.id == 3 }.isGoodMovie)
        assertTrue(result.first { it.movie.id == 2 }.isGoodMovie)
        assertFalse(result.first { it.movie.id == 1 }.isGoodMovie)
    }

    @Test
    fun `invoke should return empty list when repository is empty`() = runTest {
        val repository = FakeMoviesRepository(popularMovies = emptyList())
        val useCase = GetPopularMoviesUseCaseImpl(repository)

        val result = useCase()

        assertEquals(emptyList(), result)
    }
}

