package edu.dyds.movies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PopularMoviesViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {
    private val moviesStateMutableStateFlow = MutableStateFlow(MoviesUiState())
    val moviesStateFlow: Flow<MoviesUiState> = moviesStateMutableStateFlow

    fun getPopularMovies() {
        viewModelScope.launch {
            moviesStateMutableStateFlow.emit(
                MoviesUiState(isLoading = true)
            )
            moviesStateMutableStateFlow.emit(
                MoviesUiState(
                    isLoading = false,
                    movies = getPopularMoviesUseCase()
                )
            )
        }
    }
}
