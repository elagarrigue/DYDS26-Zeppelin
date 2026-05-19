package edu.dyds.movies.presentation.detail

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import edu.dyds.movies.presentation.detail.components.DetailContent
import edu.dyds.movies.presentation.detail.components.DetailTopBar

@Composable
fun DetailRoute(viewModel: MovieDetailsViewModel, title: String, onBack: () -> Unit) {

    LaunchedEffect(title) {
        viewModel.getMovieDetails(title)
    }

    val state by viewModel.movieDetailStateFlow.collectAsState(MovieDetailUiState())

    DetailScreen(
        state = state,
        onBack = onBack,
        onRetry = { viewModel.getMovieDetails(title) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: MovieDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    MaterialTheme {
        Surface {
            Scaffold(
                topBar = {
                    DetailTopBar(
                        title = state.movie?.title ?: "",
                        onBack = onBack,
                        scrollBehavior = scrollBehavior
                    )
                }
            ) { padding ->
                DetailContent(
                    state = state,
                    padding = padding,
                    onRetry = onRetry
                )
            }
        }
    }
}
