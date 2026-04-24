package edu.dyds.movies.presentation.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import dydsproject.composeapp.generated.resources.Res
import dydsproject.composeapp.generated.resources.app_name
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.presentation.state.MoviesUiState
import edu.dyds.movies.presentation.utils.LoadingIndicator
import edu.dyds.movies.presentation.utils.NoResults
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = { Text(stringResource(Res.string.app_name)) },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun HomeContent(
    state: MoviesUiState,
    padding: PaddingValues,
    onRetry: () -> Unit,
    onGoodMovieClick: (Movie) -> Unit
) {
    LoadingIndicator(state.isLoading)

    when {
        state.movies.isNotEmpty() -> MovieGrid(padding, state.movies, onGoodMovieClick)
        state.isLoading.not() -> NoResults(onRetry)
    }
}





