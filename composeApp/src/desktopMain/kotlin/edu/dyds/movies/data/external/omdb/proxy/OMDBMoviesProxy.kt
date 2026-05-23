package edu.dyds.movies.data.external.omdb.proxy

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class OMDBMoviesProxy(
    private val externalSource: OMDBMoviesExternalSource,
) : MovieExternalSource {
    override suspend fun getMovieByTitle(title: String): Movie? =
        runCatching { externalSource.getMovieDetails(title).toDomainMovie() }
            .getOrNull()
}

