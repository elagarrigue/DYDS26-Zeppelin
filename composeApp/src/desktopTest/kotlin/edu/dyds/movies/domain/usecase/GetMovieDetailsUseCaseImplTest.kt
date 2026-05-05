package edu.dyds.movies.domain.usecase

import edu.dyds.movies.fakes.FakeMoviesRepository
import edu.dyds.movies.fakes.movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
    }
}


