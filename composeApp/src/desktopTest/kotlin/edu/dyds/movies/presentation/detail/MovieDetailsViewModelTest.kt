package edu.dyds.movies.presentation.detail

import app.cash.turbine.test
import edu.dyds.movies.movie
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
        var initialState: MovieDetailUiState? = null

        // act
        viewModel.movieDetailStateFlow.test {
            initialState = awaitItem()
        }

        // assert
        assertEquals(MovieDetailUiState(), initialState)
    }

    @Test
    fun `getMovieDetails should emit loading before returning content`() = runTest {
        // arrange
        val expectedMovie = movie(id = 10)
        val title = expectedMovie.title
        val useCase = FakeGetMovieDetailsUseCase(movie = expectedMovie)
        val viewModel = MovieDetailsViewModel(useCase)
        var loadingState: MovieDetailUiState? = null
        var finalState: MovieDetailUiState? = null

        // act
        viewModel.movieDetailStateFlow.test {
            awaitItem()
            viewModel.getMovieDetails(title)
            loadingState = awaitItem()
            finalState = awaitItem()
        }

        // assert
        assertEquals(MovieDetailUiState(isLoading = true), loadingState)
        assertEquals(MovieDetailUiState(isLoading = false, movie = expectedMovie), finalState)
        assertEquals(1, useCase.getMovieDetailsCalls)
        assertEquals(title, useCase.lastRequestedTitle)
    }

    @Test
    fun `getMovieDetails should return null when movie not found`() = runTest {
        // arrange
        val title = "Unknown"
        val useCase = FakeGetMovieDetailsUseCase(movie = null)
        val viewModel = MovieDetailsViewModel(useCase)
        var finalState: MovieDetailUiState? = null

        // act
        viewModel.movieDetailStateFlow.test {
            awaitItem()
            viewModel.getMovieDetails(title)
            awaitItem()
            finalState = awaitItem()
        }

        // assert
        assertEquals(MovieDetailUiState(isLoading = false, movie = null), finalState)
        assertEquals(1, useCase.getMovieDetailsCalls)
        assertEquals(title, useCase.lastRequestedTitle)
    }
}

