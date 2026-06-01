package edu.dyds.movies.presentation.detail

import edu.dyds.movies.domain.entity.Movie

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
)



