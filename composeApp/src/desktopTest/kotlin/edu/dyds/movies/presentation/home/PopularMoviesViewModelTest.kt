package edu.dyds.movies.presentation.home

import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.movie
import edu.dyds.movies.presentation.FakeGetPopularMoviesUseCase
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
class PopularMoviesViewModelTest {
    private lateinit var testDispatcher: TestDispatcher

    private fun expectedQualifiedMovies(): List<QualifiedMovie> = listOf(
        QualifiedMovie(movie = movie(id = 1), isGoodMovie = true),
        QualifiedMovie(movie = movie(id = 2), isGoodMovie = false),
    )

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
        val useCase = FakeGetPopularMoviesUseCase(popularMovies = emptyList())
        val viewModel = PopularMoviesViewModel(useCase)
        val moviesStateFlow = viewModel.moviesStateFlow as MutableStateFlow<MoviesUiState>

        // act (no action)

        // assert
        assertEquals(MoviesUiState(), moviesStateFlow.value)
    }

    @Test
    fun `getPopularMovies should emit loading before returning content`() = runTest(testDispatcher) {
        // arrange
        val expectedMovies = expectedQualifiedMovies()
        val returnSignal = CompletableDeferred<Unit>()
        val useCase = FakeGetPopularMoviesUseCase(
            popularMovies = expectedMovies,
            returnSignal = returnSignal,
        )
        val viewModel = PopularMoviesViewModel(useCase)
        val moviesStateFlow = viewModel.moviesStateFlow as MutableStateFlow<MoviesUiState>

        // act
        viewModel.getPopularMovies()
        runCurrent()

        // assert
        assertEquals(MoviesUiState(isLoading = true), moviesStateFlow.value)

        returnSignal.complete(Unit)
        advanceUntilIdle()

        // assert
        assertEquals(MoviesUiState(isLoading = false, movies = expectedMovies), moviesStateFlow.value)
        assertEquals(1, useCase.getPopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies should return empty list when no movies available`() = runTest(testDispatcher) {
        // arrange
        val useCase = FakeGetPopularMoviesUseCase(popularMovies = emptyList())
        val viewModel = PopularMoviesViewModel(useCase)
        val moviesStateFlow = viewModel.moviesStateFlow as MutableStateFlow<MoviesUiState>

        // act
        viewModel.getPopularMovies()
        advanceUntilIdle()

        // assert
        assertEquals(MoviesUiState(isLoading = false, movies = emptyList()), moviesStateFlow.value)
        assertEquals(1, useCase.getPopularMoviesCalls)
    }
}

