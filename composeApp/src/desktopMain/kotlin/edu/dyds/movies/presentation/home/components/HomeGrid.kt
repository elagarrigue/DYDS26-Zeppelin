package edu.dyds.movies.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie

@Composable
fun MovieGrid(
    padding: PaddingValues,
    movies: List<QualifiedMovie>,
    onMovieClick: (Movie) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(padding)
    ) {
        items(movies, key = { it.movie.id }) { qualifiedMovie ->
            MovieGridItem(qualifiedMovie, onMovieClick)
        }
    }
}

@Composable
fun MovieGridItem(
    qualifiedMovie: QualifiedMovie,
    onMovieClick: (Movie) -> Unit
) {
    when (qualifiedMovie.isGoodMovie) {
        true -> GoodMovieItem(qualifiedMovie.movie) { onMovieClick(qualifiedMovie.movie) }
        false -> BadMovieItem(qualifiedMovie.movie)
    }
}


