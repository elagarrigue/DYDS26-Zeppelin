package edu.dyds.movies.data.external.tmdb.proxy

import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie
import edu.dyds.movies.data.external.tmdb.TMDBRemoteResult
import edu.dyds.movies.domain.entity.Movie

private fun defaultPosterPath(movie: Movie) = "/poster-${movie.id}.png"

private fun defaultBackdropPath(movie: Movie) = "/backdrop-${movie.id}.png"

fun remoteMovie(
    movie: Movie,
    releaseDate: String? = movie.releaseDate,
    posterPath: String? = defaultPosterPath(movie),
    backdropPath: String? = defaultBackdropPath(movie),
    popularity: Double? = movie.popularity,
    voteAverage: Double? = movie.voteAverage,
): TMDBRemoteMovie {
    return TMDBRemoteMovie(
        id = movie.id,
        title = movie.title,
        overview = movie.overview,
        releaseDate = releaseDate,
        posterPath = posterPath,
        backdropPath = backdropPath,
        originalTitle = movie.originalTitle,
        originalLanguage = movie.originalLanguage,
        popularity = popularity,
        voteAverage = voteAverage
    )
}

fun remoteResult(
    results: List<TMDBRemoteMovie>,
    page: Int = 1,
    totalPages: Int = 1,
    totalResults: Int = results.size,
): TMDBRemoteResult {
    return TMDBRemoteResult(
        page = page,
        results = results,
        totalPages = totalPages,
        totalResults = totalResults
    )
}
