package edu.dyds.movies

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.domain.entity.Movie

fun movie(
    id: Int,
    title: String = "Movie $id",
    overview: String = "Overview $id",
    releaseDate: String = "2024-01-01",
    poster: String = "poster-$id",
    backdrop: String? = "backdrop-$id",
    originalTitle: String = "Original $id",
    originalLanguage: String = "en",
    popularity: Double = 10.0,
    voteAverage: Double = 5.0,
): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        poster = poster,
        backdrop = backdrop,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        popularity = popularity,
        voteAverage = voteAverage
    )
}

fun remoteMovie(
    id: Int,
    title: String = "Remote $id",
    overview: String = "Remote overview $id",
    releaseDate: String = "2024-01-01",
    posterPath: String = "/poster-$id.png",
    backdropPath: String? = "/backdrop-$id.png",
    originalTitle: String = "Remote original $id",
    originalLanguage: String = "en",
    popularity: Double = 10.0,
    voteAverage: Double = 5.0,
): RemoteMovie {
    return RemoteMovie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = posterPath,
        backdropPath = backdropPath,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        popularity = popularity,
        voteAverage = voteAverage
    )
}

fun remoteResult(
    page: Int = 1,
    results: List<RemoteMovie>,
    totalPages: Int = 1,
    totalResults: Int = results.size,
): RemoteResult {
    return RemoteResult(
        page = page,
        results = results,
        totalPages = totalPages,
        totalResults = totalResults
    )
}

