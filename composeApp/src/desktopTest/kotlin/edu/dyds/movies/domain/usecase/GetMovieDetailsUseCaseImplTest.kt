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
        val title = expected.title
        val repository = FakeMoviesRepository(movieByTitle = expected)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase(title)

        // assert
        assertEquals(expected, result)
        assertEquals(1, repository.getMovieByTitleCalls)
        assertEquals(title, repository.lastRequestedTitle)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {
        // arrange
        val title = "Unknown"
        val repository = FakeMoviesRepository(movieByTitle = null)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase(title)

        // assert
        assertNull(result)
        assertEquals(1, repository.getMovieByTitleCalls)
        assertEquals(title, repository.lastRequestedTitle)
    }
}



