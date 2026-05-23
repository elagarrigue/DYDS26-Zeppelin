package edu.dyds.movies.data.external

import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class MovieExternalSourceBroker(
    private val tmdbMoviesExternalSource: TMDBMoviesExternalSource,
    private val omdbMoviesExternalSource: OMDBMoviesExternalSource,
) : MovieExternalSource {
    override suspend fun getMovieByTitle(title: String): Movie? {
        val tmdbMovie = tmdbMoviesExternalSource.getMovieByTitle(title)
        val omdbMovie = omdbMoviesExternalSource.getMovieByTitle(title)

        return when {
            tmdbMovie != null && omdbMovie != null -> buildMovie(tmdbMovie, omdbMovie)
            tmdbMovie != null -> tmdbMovie.withSourceOverview("TMDB")
            omdbMovie != null -> omdbMovie.withSourceOverview("OMDB")
            else -> null
        }
    }

    private fun buildMovie(
        tmdbMovie: Movie,
        omdbMovie: Movie,
    ): Movie =
        Movie(
            id = tmdbMovie.id,
            title = tmdbMovie.title,
            overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
            releaseDate = tmdbMovie.releaseDate,
            poster = tmdbMovie.poster,
            backdrop = tmdbMovie.backdrop,
            originalTitle = tmdbMovie.originalTitle,
            originalLanguage = tmdbMovie.originalLanguage,
            popularity = (tmdbMovie.popularity + omdbMovie.popularity) / 2.0,
            voteAverage = (tmdbMovie.voteAverage + omdbMovie.voteAverage) / 2.0,
        )

    private fun Movie.withSourceOverview(source: String): Movie =
        copy(overview = "$source: $overview")
}


