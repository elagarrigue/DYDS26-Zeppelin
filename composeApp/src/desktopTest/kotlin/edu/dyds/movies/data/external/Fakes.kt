package edu.dyds.movies.data.external

import edu.dyds.movies.FakeMovieDefaults
import edu.dyds.movies.data.external.omdb.OMDBRemoteMovie
import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie
import edu.dyds.movies.data.external.tmdb.TMDBRemoteResult

fun tmdbRemoteMovie(
    id: Int,
    title: String = FakeMovieDefaults.title(id),
    overview: String = FakeMovieDefaults.overview(id),
    releaseDate: String? = FakeMovieDefaults.RELEASE_DATE,
    posterPath: String? = FakeMovieDefaults.posterPath(id),
    backdropPath: String? = FakeMovieDefaults.backdropPath(id),
    originalTitle: String = FakeMovieDefaults.originalTitle(id),
    originalLanguage: String = FakeMovieDefaults.ORIGINAL_LANGUAGE,
    popularity: Double? = FakeMovieDefaults.POPULARITY,
    voteAverage: Double? = FakeMovieDefaults.VOTE_AVERAGE,
): TMDBRemoteMovie {
    return TMDBRemoteMovie(
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

fun tmdbRemoteResult(
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

fun omdbRemoteMovie(
    id: Int,
    title: String = FakeMovieDefaults.title(id),
    overview: String = FakeMovieDefaults.overview(id),
    released: String = FakeMovieDefaults.RELEASE_DATE,
    year: String = released.take(4),
    poster: String = FakeMovieDefaults.poster(id),
    language: String = FakeMovieDefaults.ORIGINAL_LANGUAGE,
    metaScore: String = FakeMovieDefaults.VOTE_AVERAGE.toString(),
    imdbRating: String = FakeMovieDefaults.POPULARITY.toString(),
): OMDBRemoteMovie {
    return OMDBRemoteMovie(
        title = title,
        plot = overview,
        released = released,
        year = year,
        poster = poster,
        language = language,
        metaScore = metaScore,
        imdbRating = imdbRating
    )
}

