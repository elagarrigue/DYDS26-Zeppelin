package edu.dyds.movies.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import org.jetbrains.compose.resources.stringResource

@Composable
fun GoodMovieItem(movie: Movie, onClick: () -> Unit) {
    MoviePosterItem(
        movie = movie,
        modifier = Modifier,
        onClick = onClick
    )
}

@Composable
fun BadMovieItem(movie: Movie) {
    var dialogState by remember { mutableStateOf(false) }

    MoviePosterItem(
        movie = movie,
        modifier = Modifier.alpha(0.7f),
        onClick = { dialogState = true }
    )

    BadMovieDialog(visible = dialogState, onCloseRequest = { dialogState = false })
}

@Composable
fun BadMovieDialog(
    visible: Boolean,
    onCloseRequest: () -> Unit
) {
    DialogWindow(
        title = stringResource(Res.string.error),
        resizable = false,
        onCloseRequest = onCloseRequest,
        visible = visible
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
fun MoviePosterItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClick() }
    ) {
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


