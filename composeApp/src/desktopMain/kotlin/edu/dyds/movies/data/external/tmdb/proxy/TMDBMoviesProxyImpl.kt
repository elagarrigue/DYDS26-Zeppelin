package edu.dyds.movies.data.external.tmdb.proxy

import edu.dyds.movies.data.external.MovieDetailsExternalSource
import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie
import edu.dyds.movies.domain.entity.Movie

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p"

internal interface TMDBMoviesProxy : MovieDetailsExternalSource

internal class TMDBMoviesProxyImpl(
    private val externalSource: TMDBMoviesExternalSource,
) : PopularMoviesExternalSource, TMDBMoviesProxy {
    override suspend fun getPopularMovies(): List<Movie> =
        runCatching { externalSource.getPopularMovies().results.map { it.toDomainMovie() } }
            .getOrDefault(emptyList())

    override suspend fun getMovieByTitle(title: String): Movie? =
        runCatching { externalSource.getMovie(title).toDomainMovie() }
            .getOrNull()
}

private fun TMDBRemoteMovie.toDomainMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate.orEmpty(),
        poster = posterPath?.let { "$TMDB_IMAGE_BASE_URL/w185$it" }.orEmpty(),
        backdrop = backdropPath?.let { "$TMDB_IMAGE_BASE_URL/w780$it" }.orEmpty(),
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        popularity = popularity ?: 0.0,
        voteAverage = voteAverage ?: 0.0
    )
}
