package edu.dyds.movies.presentation.detail

import app.cash.turbine.test
import edu.dyds.movies.movieFromSeed
import edu.dyds.movies.presentation.FakeGetMovieDetailsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDetailsViewModelTest {

    @Test
    fun `initial state should be empty and not loading`() = runTest {
        // arrange
        val useCase = FakeGetMovieDetailsUseCase(movie = null)
        val viewModel = MovieDetailsViewModel(useCase)
        var initialState: MovieDetailsUiState? = null

        // act
        viewModel.movieDetailsStateFlow.test {
            initialState = awaitItem()
        }

        // assert
        assertEquals(MovieDetailsUiState(), initialState)
    }

    @Test
    fun `getMovieDetails should emit loading before returning content`() = runTest {
        // arrange
        val expectedMovie = movieFromSeed(seed = 10)
        val useCase = FakeGetMovieDetailsUseCase(movie = expectedMovie)
        val viewModel = MovieDetailsViewModel(useCase)
        var loadingState: MovieDetailsUiState? = null
        var finalState: MovieDetailsUiState? = null

        // act
        viewModel.movieDetailsStateFlow.test {
            awaitItem()
            viewModel.getMovieDetails(expectedMovie.title)
            loadingState = awaitItem()
            finalState = awaitItem()
        }

        // assert
        assertEquals(MovieDetailsUiState(isLoading = true), loadingState)
        assertEquals(MovieDetailsUiState(isLoading = false, movie = expectedMovie), finalState)
        assertEquals(1, useCase.getMovieDetailsCalls)
        assertEquals(expectedMovie.title, useCase.lastRequestedTitle)
    }

    @Test
    fun `getMovieDetails should return null when movie not found`() = runTest {
        // arrange
        val title = "Unknown"
        val useCase = FakeGetMovieDetailsUseCase(movie = null)
        val viewModel = MovieDetailsViewModel(useCase)
        var finalState: MovieDetailsUiState? = null

        // act
        viewModel.movieDetailsStateFlow.test {
            awaitItem()
            viewModel.getMovieDetails(title)
            awaitItem()
            finalState = awaitItem()
        }

        // assert
        assertEquals(MovieDetailsUiState(isLoading = false, movie = null), finalState)
        assertEquals(1, useCase.getMovieDetailsCalls)
        assertEquals(title, useCase.lastRequestedTitle)
    }
}

