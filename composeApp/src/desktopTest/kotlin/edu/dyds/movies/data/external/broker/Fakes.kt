package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.omdb.OMDBRemoteMovie
import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie
import edu.dyds.movies.domain.entity.Movie

fun remoteMovie(movie: Movie) =
    TMDBRemoteMovie(
        id = movie.id,
        title = movie.title,
        overview = movie.overview,
        releaseDate = movie.releaseDate,
        posterPath = null,
        backdropPath = null,
        originalTitle = movie.originalTitle,
        originalLanguage = movie.originalLanguage,
        popularity = movie.popularity,
        voteAverage = movie.voteAverage
    )

fun remoteOmdbMovie(movie: Movie, posterUrl: String = "") =
    OMDBRemoteMovie(
        title = movie.title,
        year = movie.releaseDate.take(4),
        released = movie.releaseDate,
        plot = movie.overview,
        poster = posterUrl,
        imdbRating = movie.popularity.toString(),
        metaScore = movie.voteAverage.toString(),
        language = movie.originalLanguage
    )


