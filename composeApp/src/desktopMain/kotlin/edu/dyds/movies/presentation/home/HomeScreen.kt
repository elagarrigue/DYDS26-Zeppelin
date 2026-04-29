package edu.dyds.movies.presentation.home

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.presentation.home.components.HomeContent
import edu.dyds.movies.presentation.home.components.HomeTopBar
import edu.dyds.movies.presentation.state.MoviesUiState
import edu.dyds.movies.presentation.viewmodel.PopularMoviesViewModel

@Composable
fun HomeRoute(
    viewModel: PopularMoviesViewModel,
    onGoodMovieClick: (Movie) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getPopularMovies()
    }

    val state by viewModel.moviesStateFlow.collectAsState(MoviesUiState())

    HomeScreen(
        state = state,
        onRetry = viewModel::getPopularMovies,
        onGoodMovieClick = onGoodMovieClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: MoviesUiState,
    onRetry: () -> Unit,
    onGoodMovieClick: (Movie) -> Unit
) {

    MaterialTheme {
        Surface {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            Scaffold(
                topBar = { HomeTopBar(scrollBehavior) },
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ) { padding ->
                HomeContent(
                    state = state,
                    padding = padding,
                    onRetry = onRetry,
                    onGoodMovieClick = onGoodMovieClick
                )
            }
        }
    }
}

