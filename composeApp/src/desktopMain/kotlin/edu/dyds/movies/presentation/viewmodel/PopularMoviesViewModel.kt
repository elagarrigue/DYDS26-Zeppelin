package edu.dyds.movies.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PopularMoviesViewModel(
    private val getPopularMovies: GetPopularMoviesUseCase,
) : ViewModel() {
    private val moviesStateMutableStateFlow =
        MutableStateFlow(UiState<List<QualifiedMovie>>(domain = emptyList()))
    val moviesStateFlow: Flow<UiState<List<QualifiedMovie>>> = moviesStateMutableStateFlow

    fun getAllMovies() {
        viewModelScope.launch {
            moviesStateMutableStateFlow.emit(
                UiState(isLoading = true)
            )
            moviesStateMutableStateFlow.emit(
                UiState(
                    isLoading = false,
                    domain = getPopularMovies()
                )
            )
        }
    }
}