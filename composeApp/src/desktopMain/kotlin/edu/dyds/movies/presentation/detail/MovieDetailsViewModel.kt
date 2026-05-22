package edu.dyds.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {
    private val _movieDetailUiState = MutableStateFlow(MovieDetailUiState())
    val movieDetailStateFlow: StateFlow<MovieDetailUiState> = _movieDetailUiState

    fun getMovieDetails(title: String) {
        viewModelScope.launch {
            _movieDetailUiState.emit(
                MovieDetailUiState(isLoading = true)
            )
            _movieDetailUiState.emit(
                MovieDetailUiState(
                    isLoading = false,
                    movie = getMovieDetailsUseCase(title)
                )
            )
        }
    }
}
