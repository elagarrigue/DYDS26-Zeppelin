package edu.dyds.movies.presentation.state

data class UiState<Domain>(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val domain: Domain? = null,
)