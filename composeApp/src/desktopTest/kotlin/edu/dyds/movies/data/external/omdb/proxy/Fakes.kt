package edu.dyds.movies.data.external.omdb.proxy

import edu.dyds.movies.data.external.omdb.OMDBRemoteMovie
import edu.dyds.movies.domain.entity.Movie

fun remoteMovie(
    movie: Movie,
    released: String = movie.releaseDate,
    year: String = movie.releaseDate.take(4),
    poster: String = movie.poster,
    language: String = movie.originalLanguage,
    metaScore: String = movie.voteAverage.toString(),
    imdbRating: String = movie.popularity.toString(),
): OMDBRemoteMovie {
    return OMDBRemoteMovie(
        title = movie.title,
        plot = movie.overview,
        released = released,
        year = year,
        poster = poster,
        language = language,
        metaScore = metaScore,
        imdbRating = imdbRating
    )
}
