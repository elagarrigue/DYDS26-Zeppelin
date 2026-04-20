@file:Suppress("FunctionName")

package edu.dyds.movies.presentation.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.dyds.movies.presentation.state.UiState

@Composable
fun LoadingIndicator(enabled: Boolean, modifier: Modifier = Modifier) {
    if (enabled) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun NoResults(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Results",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun ErrorContent(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun <Domain> UiStateContent(
    state: UiState<Domain>,
    onRetry: () -> Unit,
    isEmpty: (Domain?) -> Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (Domain, Modifier) -> Unit
) {
    LoadingIndicator(enabled = state.isLoading, modifier = modifier)

    when {
        state.hasError && state.isLoading.not() -> ErrorContent(onRetry = onRetry, modifier = modifier)
        state.domain != null && isEmpty(state.domain).not() -> content(state.domain, modifier)
        state.isLoading.not() -> NoResults(onRetry = onRetry, modifier = modifier)
    }
}
