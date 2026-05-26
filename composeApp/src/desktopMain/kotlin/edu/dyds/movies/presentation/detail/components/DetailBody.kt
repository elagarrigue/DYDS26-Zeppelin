package edu.dyds.movies.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dydsproject.composeapp.generated.resources.Res
import dydsproject.composeapp.generated.resources.original_language
import dydsproject.composeapp.generated.resources.original_title
import dydsproject.composeapp.generated.resources.popularity
import dydsproject.composeapp.generated.resources.release_date
import dydsproject.composeapp.generated.resources.vote_average
import edu.dyds.movies.domain.entity.Movie
import org.jetbrains.compose.resources.stringResource

@Composable
fun MovieDetails(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        DetailsBackdrop(movie = movie)
        DetailsOverview(overview = movie.overview)
        DetailsMetadata(movie = movie)
    }
}

@Composable
fun DetailsBackdrop(movie: Movie) {
    AsyncImage(
        model = movie.backdrop ?: movie.poster,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
    )
}

@Composable
fun DetailsOverview(overview: String) {
    Text(
        text = overview,
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
fun DetailsMetadata(movie: Movie) {
    Text(
        text = buildAnnotatedString {
            property(stringResource(Res.string.original_language), movie.originalLanguage)
            property(stringResource(Res.string.original_title), movie.originalTitle)
            property(stringResource(Res.string.popularity), movie.popularity.toString())
            property(stringResource(Res.string.release_date), movie.releaseDate)
            property(stringResource(Res.string.vote_average), movie.voteAverage.toString(), end = true)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp),
    )
}

fun AnnotatedString.Builder.property(name: String, value: String, end: Boolean = false) {
    withStyle(ParagraphStyle(lineHeight = 18.sp)) {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("$name: ")
        }
        append(value)
        if (!end) append("\n")
    }
}


