package edu.dyds.movies.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.dyds.movies.di.MoviesDependencyInjector.getMovieDetailsViewModel
import edu.dyds.movies.di.MoviesDependencyInjector.getPopularMoviesViewModel
import edu.dyds.movies.presentation.detail.DetailsRoute
import edu.dyds.movies.presentation.home.HomeRoute
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val HOME = "home"
private const val DETAIL = "detail"
private const val MOVIE_TITLE = "movieTitle"

private fun encodeRouteValue(value: String): String {
    return URLEncoder.encode(value, StandardCharsets.UTF_8.name())
}

private fun decodeRouteValue(value: String): String {
    return URLDecoder.decode(value, StandardCharsets.UTF_8.name())
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HOME) {
        homeDestination(navController)
        detailDestination(navController)
    }
}

private fun NavGraphBuilder.homeDestination(
    navController: NavHostController,
) {
    composable(HOME) {
        HomeRoute(
            viewModel = getPopularMoviesViewModel(),
            onGoodMovieClick = {
                navController.navigate("$DETAIL/${encodeRouteValue(it.title)}")
            }
        )
    }
}

private fun NavGraphBuilder.detailDestination(
    navController: NavHostController,
) {
    composable(
        route = "$DETAIL/{$MOVIE_TITLE}",
        arguments = listOf(navArgument(MOVIE_TITLE) { type = NavType.StringType })
    ) { backstackEntry ->
        val movieTitle = backstackEntry.arguments?.getString(MOVIE_TITLE)

        movieTitle?.let {
            DetailsRoute(
                viewModel = getMovieDetailsViewModel(),
                title = decodeRouteValue(it),
                onBack = { navController.popBackStack() }
            )
        }
    }
}

