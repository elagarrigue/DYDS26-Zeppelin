package edu.dyds.movies.presentation.detail

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import edu.dyds.movies.presentation.detail.components.DetailsContent
import edu.dyds.movies.presentation.detail.components.DetailsTopBar

@Composable
fun DetailsRoute(viewModel: MovieDetailsViewModel, title: String, onBack: () -> Unit) {

    LaunchedEffect(title) {
        viewModel.getMovieDetails(title)
    }

    val state by viewModel.movieDetailsStateFlow.collectAsState(MovieDetailsUiState())

    DetailsScreen(
        state = state,
        onBack = onBack,
        onRetry = { viewModel.getMovieDetails(title) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    state: MovieDetailsUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    MaterialTheme {
        Surface {
            Scaffold(
                topBar = {
                    DetailsTopBar(
                        title = state.movie?.title ?: "",
                        onBack = onBack,
                        scrollBehavior = scrollBehavior
                    )
                }
            ) { padding ->
                DetailsContent(
                    state = state,
                    padding = padding,
                    onRetry = onRetry
                )
            }
        }
    }
}
