@file:Suppress("FunctionName")

package edu.dyds.movies.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.presentation.home.components.HomeTopBar
import edu.dyds.movies.presentation.home.components.MovieGrid
import edu.dyds.movies.presentation.state.UiState
import edu.dyds.movies.presentation.utils.UiStateContent
import edu.dyds.movies.presentation.viewmodel.PopularMoviesViewModel

@Composable
fun HomeRoute(
    viewModel: PopularMoviesViewModel,
    onGoodMovieClick: (Movie) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getAllMovies()
    }

    val state by viewModel.moviesStateFlow.collectAsState(UiState(domain = emptyList()))

    HomeScreen(
        state = state,
        onRetry = viewModel::getAllMovies,
        onGoodMovieClick = onGoodMovieClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: UiState<List<QualifiedMovie>>,
    onRetry: () -> Unit,
    onGoodMovieClick: (Movie) -> Unit
) {
    MaterialTheme {
        Surface {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            Scaffold(
                topBar = {
                    HomeTopBar(scrollBehavior = scrollBehavior)
                },
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ) { padding ->
                UiStateContent(
                    state = state,
                    onRetry = onRetry,
                    isEmpty = { it.isNullOrEmpty() },
                    modifier = Modifier.padding(padding)
                ) { movies, contentModifier ->
                    MovieGrid(PaddingValues(0.dp), movies, onGoodMovieClick, contentModifier)
                }
            }
        }
    }
}


