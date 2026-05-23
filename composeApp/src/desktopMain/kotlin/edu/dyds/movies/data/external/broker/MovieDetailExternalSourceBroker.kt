package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class MovieDetailExternalSourceBroker(
    private val tmdbMovieDetailExternalSource: MovieDetailExternalSource,
    private val omdbMovieDetailExternalSource: MovieDetailExternalSource,
) : MovieDetailExternalSource {
    override suspend fun getMovieByTitle(title: String): Movie? {
        val tmdbMovie = tmdbMovieDetailExternalSource.getMovieByTitle(title)
        val omdbMovie = omdbMovieDetailExternalSource.getMovieByTitle(title)

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



