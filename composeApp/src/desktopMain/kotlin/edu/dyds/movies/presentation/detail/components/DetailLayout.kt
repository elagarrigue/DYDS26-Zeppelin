package edu.dyds.movies.presentation.detail.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.dyds.movies.presentation.detail.MovieDetailUiState
import edu.dyds.movies.presentation.utils.LoadingIndicator
import edu.dyds.movies.presentation.utils.NoResults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    state: MovieDetailUiState,
    padding: PaddingValues,
    onRetry: () -> Unit
) {
    LoadingIndicator(enabled = state.isLoading, modifier = Modifier.padding(padding))

    when {
        state.movie != null -> MovieDetail(movie = state.movie, modifier = Modifier.padding(padding))
        state.isLoading.not() -> NoResults(onRetry)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    title: String,
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        scrollBehavior = scrollBehavior
    )
}
