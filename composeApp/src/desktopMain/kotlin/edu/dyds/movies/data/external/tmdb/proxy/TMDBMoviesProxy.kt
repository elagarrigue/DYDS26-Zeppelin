package edu.dyds.movies.data.external.tmdb.proxy

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class TMDBMoviesProxy(
    private val externalSource: TMDBMoviesExternalSource,
) : MoviesExternalSource, MovieExternalSource {
    override suspend fun getPopularMovies(): List<Movie> =
        runCatching { externalSource.getPopularMoviesResult().toDomainMovieList() }
            .getOrDefault(emptyList())

    override suspend fun getMovieByTitle(title: String): Movie? =
        runCatching { externalSource.getMovieDetailsResult(title).results.first().toDomainMovie() }
            .getOrNull()
}

