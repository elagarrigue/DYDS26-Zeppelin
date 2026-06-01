package edu.dyds.movies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PopularMoviesViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {
    private val _moviesUiState = MutableStateFlow(MoviesUiState())
    val moviesStateFlow: StateFlow<MoviesUiState> = _moviesUiState

    fun getPopularMovies() {
        viewModelScope.launch {
            _moviesUiState.emit(
                MoviesUiState(isLoading = true)
            )
            _moviesUiState.emit(
                MoviesUiState(
                    isLoading = false,
                    movies = getPopularMoviesUseCase()
                )
            )
        }
    }
}
