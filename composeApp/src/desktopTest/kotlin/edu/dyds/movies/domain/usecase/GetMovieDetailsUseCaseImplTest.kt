package edu.dyds.movies.domain.usecase

import edu.dyds.movies.testdoubles.FakeMoviesRepository
import edu.dyds.movies.testdoubles.movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieDetailsUseCaseImplTest {

    @Test
    fun `invoke should return movie details from repository`() = runTest {
        val expected = movie(id = 10)
        val repository = FakeMoviesRepository(movieDetails = expected)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        val result = useCase(10)

        assertEquals(expected, result)
    }
}

