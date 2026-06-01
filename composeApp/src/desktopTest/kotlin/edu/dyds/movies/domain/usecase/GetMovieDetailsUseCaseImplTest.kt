package edu.dyds.movies.domain.usecase

import edu.dyds.movies.movieFromSeed
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetMovieDetailsUseCaseImplTest {

    @Test
    fun `invoke should return movie details from repository`() = runTest {
        // arrange
        val expectedMovie = movieFromSeed(seed = 10)
        val repository = FakeMoviesRepository(movieByTitle = expectedMovie)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase(expectedMovie.title)

        // assert
        assertEquals(expectedMovie, result)
        assertEquals(1, repository.getMovieByTitleCalls)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {
        // arrange
        val repository = FakeMoviesRepository(movieByTitle = null)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase("Unknown movie")

        // assert
        assertNull(result)
        assertEquals(1, repository.getMovieByTitleCalls)
    }
}



