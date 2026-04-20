package edu.dyds.movies.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.presentation.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieDetails: GetMovieDetailsUseCase
) : ViewModel() {
    private val movieDetailStateMutableStateFlow = MutableStateFlow(UiState<Movie>())
    val movieDetailStateFlow: Flow<UiState<Movie>> = movieDetailStateMutableStateFlow

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            movieDetailStateMutableStateFlow.emit(
                UiState(isLoading = true)
            )
            movieDetailStateMutableStateFlow.emit(
                UiState(
                    isLoading = false,
                    domain = getMovieDetails(id)
                )
            )
        }
    }
}