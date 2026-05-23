package edu.dyds.movies.data.external.omdb.proxy

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class OMDBMoviesProxy(
    private val externalSource: OMDBMoviesExternalSource,
) : MovieDetailExternalSource {
    override suspend fun getMovieByTitle(title: String): Movie? =
        runCatching { externalSource.getMovieDetailResult(title).toDomainMovie() }
            .getOrNull()
}

