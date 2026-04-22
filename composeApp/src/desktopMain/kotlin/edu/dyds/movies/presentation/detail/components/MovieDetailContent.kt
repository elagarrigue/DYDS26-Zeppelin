@file:Suppress("FunctionName")

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
fun MovieDetailContent(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        AsyncImage(
            model = movie.backdrop ?: movie.poster,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )
        Text(
            text = movie.overview,
            modifier = Modifier.padding(16.dp)
        )
        MovieMetadata(
            text = buildMovieMetadataText(movie = movie),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
        )
    }
}

@Composable
private fun buildMovieMetadataText(movie: Movie): AnnotatedString {
    val entries = listOf(
        MovieMetadataEntry(
            label = stringResource(Res.string.original_language),
            value = movie.originalLanguage
        ),
        MovieMetadataEntry(
            label = stringResource(Res.string.original_title),
            value = movie.originalTitle
        ),
        MovieMetadataEntry(
            label = stringResource(Res.string.popularity),
            value = movie.popularity.toString()
        ),
        MovieMetadataEntry(
            label = stringResource(Res.string.release_date),
            value = movie.releaseDate
        ),
        MovieMetadataEntry(
            label = stringResource(Res.string.vote_average),
            value = movie.voteAverage.toString()
        )
    )

    return buildAnnotatedString {
        entries.forEachIndexed { index, entry ->
            appendPropertyLine(label = entry.label, value = entry.value)
            if (index < entries.lastIndex) {
                append("\n")
            }
        }
    }
}

@Composable
private fun MovieMetadata(
    text: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Text(text = text, modifier = modifier)
}

private fun AnnotatedString.Builder.appendPropertyLine(label: String, value: String) {
    withStyle(ParagraphStyle(lineHeight = 18.sp)) {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(label)
            append(": ")
        }
        append(value)
    }
}

private data class MovieMetadataEntry(
    val label: String,
    val value: String
)

