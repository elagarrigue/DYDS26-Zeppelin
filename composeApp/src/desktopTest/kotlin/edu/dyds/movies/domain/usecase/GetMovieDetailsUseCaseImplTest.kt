package edu.dyds.movies.domain.usecase

import edu.dyds.movies.movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetMovieDetailsUseCaseImplTest {

    @Test
    fun `invoke should return movie details from repository`() = runTest {
        // arrange
        val expected = movie(id = 10)
        val repository = FakeMoviesRepository(movieDetails = expected)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase(10)

        // assert
        assertEquals(expected, result)
        assertEquals(1, repository.getMovieDetailsCalls)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {
        // arrange
        val repository = FakeMoviesRepository(movieDetails = null)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase(10)

        // assert
        assertNull(result)
        assertEquals(1, repository.getMovieDetailsCalls)
    }
}



