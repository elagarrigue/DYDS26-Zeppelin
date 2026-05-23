package edu.dyds.movies.data.external.tmdb.proxy

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class TMDBMoviesProxy(
    private val externalSource: TMDBMoviesExternalSource,
) : PopularMoviesExternalSource, MovieDetailExternalSource {
    override suspend fun getPopularMovies(): List<Movie> =
        runCatching { externalSource.getPopularMoviesResult().toDomainMovieList() }
            .getOrDefault(emptyList())

    override suspend fun getMovieByTitle(title: String): Movie? =
        runCatching { externalSource.getMovieDetailResult(title).results.first().toDomainMovie() }
            .getOrNull()
}

