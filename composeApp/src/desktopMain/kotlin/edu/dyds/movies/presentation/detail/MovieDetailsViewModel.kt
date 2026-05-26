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
    private val _movieDetailsUiState = MutableStateFlow(MovieDetailsUiState())
    val movieDetailsStateFlow: StateFlow<MovieDetailsUiState> = _movieDetailsUiState

    fun getMovieDetails(title: String) {
        viewModelScope.launch {
            _movieDetailsUiState.emit(
                MovieDetailsUiState(isLoading = true)
            )
            _movieDetailsUiState.emit(
                MovieDetailsUiState(
                    isLoading = false,
                    movie = getMovieDetailsUseCase(title)
                )
            )
        }
    }
}
