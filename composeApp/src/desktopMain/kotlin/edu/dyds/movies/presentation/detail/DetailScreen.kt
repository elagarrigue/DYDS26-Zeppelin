@file:Suppress("FunctionName")

package edu.dyds.movies.presentation.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.presentation.detail.components.DetailTopBar
import edu.dyds.movies.presentation.detail.components.MovieDetailContent
import edu.dyds.movies.presentation.state.UiState
import edu.dyds.movies.presentation.utils.UiStateContent
import edu.dyds.movies.presentation.viewmodel.MovieDetailsViewModel

@Composable
fun DetailRoute(viewModel: MovieDetailsViewModel, id: Int, onBack: () -> Unit) {

    LaunchedEffect(id) {
        viewModel.getMovieDetail(id)
    }

    val state by viewModel.movieDetailStateFlow.collectAsState(UiState<Movie>())

    DetailScreen(
        state = state,
        onBack = onBack,
        onRetry = { viewModel.getMovieDetail(id) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: UiState<Movie>,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    MaterialTheme {
        Surface {
            Scaffold(
                topBar = {
                    DetailTopBar(
                        title = state.domain?.title ?: "",
                        onBack = onBack,
                        scrollBehavior = scrollBehavior
                    )
                }
            ) { padding ->
                UiStateContent(
                    state = state,
                    onRetry = onRetry,
                    isEmpty = { it == null },
                    modifier = Modifier.padding(padding)
                ) { movie, contentModifier ->
                    MovieDetailContent(movie = movie, modifier = contentModifier)
                }
            }
        }
    }
}
