@file:Suppress("FunctionName")

package edu.dyds.movies.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import coil3.compose.AsyncImage
import dydsproject.composeapp.generated.resources.Res
import dydsproject.composeapp.generated.resources.error
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import org.jetbrains.compose.resources.stringResource

@Composable
fun MovieGrid(
    padding: PaddingValues,
    movies: List<QualifiedMovie>,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(padding)
    ) {
        items(movies, key = { it.movie.id }) { qualifiedMovie ->
            MovieGridItem(
                qualifiedMovie = qualifiedMovie,
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
private fun MovieGridItem(
    qualifiedMovie: QualifiedMovie,
    onMovieClick: (Movie) -> Unit
) {
    if (qualifiedMovie.isGoodMovie) {
        MoviePosterItem(
            movie = qualifiedMovie.movie,
            onClick = { onMovieClick(qualifiedMovie.movie) }
        )
        return
    }

    var isBadMovieDialogVisible by remember { mutableStateOf(false) }

    MoviePosterItem(
        movie = qualifiedMovie.movie,
        modifier = Modifier.alpha(0.7f),
        onClick = { isBadMovieDialogVisible = true }
    )

    if (isBadMovieDialogVisible) {
        BadMovieDialog(onClose = { isBadMovieDialogVisible = false })
    }
}

@Composable
private fun BadMovieDialog(
    onClose: () -> Unit
) {
    DialogWindow(
        title = stringResource(Res.string.error),
        resizable = false,
        onCloseRequest = onClose,
        visible = true
    ) {
        Image(
            painter = painterResource("images/too_bad.png"),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}

@Composable
private fun MoviePosterItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        AsyncImage(
            model = movie.poster,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.small)
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            modifier = Modifier.padding(8.dp)
        )
    }
}


