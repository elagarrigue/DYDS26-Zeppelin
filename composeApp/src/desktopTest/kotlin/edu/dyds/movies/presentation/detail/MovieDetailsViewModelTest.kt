package edu.dyds.movies.presentation.detail

import edu.dyds.movies.movie
import edu.dyds.movies.presentation.FakeGetMovieDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {
    private lateinit var testDispatcher: TestDispatcher

    @BeforeTest
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty and not loading`() = runTest(testDispatcher) {
        // arrange
        val useCase = FakeGetMovieDetailsUseCase(movie = null)
        val viewModel = MovieDetailsViewModel(useCase)
        val movieDetailStateFlow = viewModel.movieDetailStateFlow as MutableStateFlow<MovieDetailUiState>

        // assert
        assertEquals(MovieDetailUiState(), movieDetailStateFlow.value)
    }

    @Test
    fun `getMovieDetails should emit loading before returning content`() = runTest(testDispatcher) {
        // arrange
        val expectedMovie = movie(id = 10)
        val returnSignal = CompletableDeferred<Unit>()
        val useCase = FakeGetMovieDetailsUseCase(
            movie = expectedMovie,
            returnSignal = returnSignal,
        )
        val viewModel = MovieDetailsViewModel(useCase)
        val movieDetailStateFlow = viewModel.movieDetailStateFlow as MutableStateFlow<MovieDetailUiState>

        // act
        viewModel.getMovieDetails(10)
        runCurrent()

        // assert
        assertEquals(MovieDetailUiState(isLoading = true), movieDetailStateFlow.value)
        assertEquals(1, useCase.getMovieDetailsCalls)
        assertEquals(10, useCase.lastRequestedId)

        returnSignal.complete(Unit)
        advanceUntilIdle()

        // assert
        assertEquals(MovieDetailUiState(isLoading = false, movie = expectedMovie), movieDetailStateFlow.value)
    }

    @Test
    fun `getMovieDetails should return movie with loading false after completion`() = runTest(testDispatcher) {
        // arrange
        val expectedMovie = movie(id = 10)
        val useCase = FakeGetMovieDetailsUseCase(movie = expectedMovie)
        val viewModel = MovieDetailsViewModel(useCase)
        val movieDetailStateFlow = viewModel.movieDetailStateFlow as MutableStateFlow<MovieDetailUiState>

        // act
        viewModel.getMovieDetails(10)
        advanceUntilIdle()

        // assert
        assertEquals(MovieDetailUiState(isLoading = false, movie = expectedMovie), movieDetailStateFlow.value)
        assertEquals(1, useCase.getMovieDetailsCalls)
        assertEquals(10, useCase.lastRequestedId)
    }

    @Test
    fun `getMovieDetails should return null when movie not found`() = runTest(testDispatcher) {
        // arrange
        val useCase = FakeGetMovieDetailsUseCase(movie = null)
        val viewModel = MovieDetailsViewModel(useCase)
        val movieDetailStateFlow = viewModel.movieDetailStateFlow as MutableStateFlow<MovieDetailUiState>

        // act
        viewModel.getMovieDetails(999)
        advanceUntilIdle()

        // assert
        assertEquals(MovieDetailUiState(isLoading = false, movie = null), movieDetailStateFlow.value)
        assertEquals(1, useCase.getMovieDetailsCalls)
        assertEquals(999, useCase.lastRequestedId)
    }
}



