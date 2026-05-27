package edu.dyds.movies.data.external.omdb.proxy

import edu.dyds.movies.data.external.MovieDetailsExternalSource
import edu.dyds.movies.data.external.omdb.OMDBRemoteMovie
import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

internal interface OMDBMoviesProxy : MovieDetailsExternalSource

internal class OMDBMoviesProxyImpl(
    private val externalSource: OMDBMoviesExternalSource,
) : OMDBMoviesProxy {
    override suspend fun getMovieByTitle(title: String): Movie? =
        runCatching { externalSource.getMovie(title).toDomainMovie() }
            .getOrNull()
}

private fun OMDBRemoteMovie.toDomainMovie(): Movie {
    val release = if (released.isNotEmpty() && released != "N/A") released else year
    val voteAverage = if (metaScore.isNotEmpty() && metaScore != "N/A") metaScore.toDouble() else 0.0
    val popularity = if (imdbRating.isNotEmpty() && imdbRating != "N/A") imdbRating.toDouble() else 0.0

    return Movie(
        id = title.hashCode(),
        title = title,
        overview = plot,
        releaseDate = release,
        poster = poster,
        backdrop = poster,
        originalTitle = title,
        originalLanguage = language,
        popularity = popularity,
        voteAverage = voteAverage
    )
}
