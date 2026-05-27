package edu.dyds.movies.presentation.home

import app.cash.turbine.test
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.movieFromSeed
import edu.dyds.movies.presentation.FakeGetPopularMoviesUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PopularMoviesViewModelTest {

    private fun expectedQualifiedMovies(): List<QualifiedMovie> = listOf(
        QualifiedMovie(movie = movieFromSeed(seed = 1), isGoodMovie = true),
        QualifiedMovie(movie = movieFromSeed(seed = 2), isGoodMovie = false),
    )

    @Test
    fun `initial state should be empty and not loading`() = runTest {
        // arrange
        val useCase = FakeGetPopularMoviesUseCase(popularMovies = emptyList())
        val viewModel = PopularMoviesViewModel(useCase)
        var initialState: MoviesUiState? = null

        // act
        viewModel.moviesStateFlow.test {
            initialState = awaitItem()
        }

        // assert
        assertEquals(MoviesUiState(), initialState)
    }

    @Test
    fun `getPopularMovies should emit loading before returning content`() = runTest {
        // arrange
        val expectedMovies = expectedQualifiedMovies()
        val useCase = FakeGetPopularMoviesUseCase(popularMovies = expectedMovies)
        val viewModel = PopularMoviesViewModel(useCase)
        var loadingState: MoviesUiState? = null
        var finalState: MoviesUiState? = null

        // act
        viewModel.moviesStateFlow.test {
            awaitItem()
            viewModel.getPopularMovies()
            loadingState = awaitItem()
            finalState = awaitItem()
        }

        // assert
        assertEquals(MoviesUiState(isLoading = true), loadingState)
        assertEquals(MoviesUiState(isLoading = false, movies = expectedMovies), finalState)
        assertEquals(1, useCase.getPopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should return empty list when no movies available`() = runTest {
        // arrange
        val useCase = FakeGetPopularMoviesUseCase(popularMovies = emptyList())
        val viewModel = PopularMoviesViewModel(useCase)
        var finalState: MoviesUiState? = null

        // act
        viewModel.moviesStateFlow.test {
            awaitItem()
            viewModel.getPopularMovies()
            awaitItem()
            finalState = awaitItem()
        }

        // assert
        assertEquals(MoviesUiState(isLoading = false, movies = emptyList()), finalState)
        assertEquals(1, useCase.getPopularMoviesCalls)
    }
}

