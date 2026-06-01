package edu.dyds.movies

import edu.dyds.movies.data.external.omdb.OMDBRemoteMovie
import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie
import edu.dyds.movies.data.external.tmdb.TMDBRemoteResult
import edu.dyds.movies.domain.entity.Movie

internal object FakeMovieDefaults {
    const val RELEASE_DATE = "2024-01-01"
    const val ORIGINAL_LANGUAGE = "en"
    const val POPULARITY = 10.0
    const val VOTE_AVERAGE = 5.0

    fun title(seed: Int) = "Movie $seed"
    fun overview(seed: Int) = "Overview $seed"
    fun originalTitle(seed: Int) = "Original $seed"
    fun poster(seed: Int) = "poster-$seed"
    fun backdrop(seed: Int) = "backdrop-$seed"
    fun posterPath(seed: Int) = "/poster-$seed.png"
    fun backdropPath(seed: Int) = "/backdrop-$seed.png"
}

fun movieFromSeedAsOmdb(
    seed: Int,
    title: String = FakeMovieDefaults.title(seed),
    id: Int = title.hashCode(),
    overview: String = FakeMovieDefaults.overview(seed),
    releaseDate: String = FakeMovieDefaults.RELEASE_DATE,
    poster: String = FakeMovieDefaults.poster(seed),
    originalLanguage: String = FakeMovieDefaults.ORIGINAL_LANGUAGE,
    popularity: Double = FakeMovieDefaults.POPULARITY,
    voteAverage: Double = FakeMovieDefaults.VOTE_AVERAGE,
): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        poster = poster,
        backdrop = poster,
        originalTitle = title,
        originalLanguage = originalLanguage,
        popularity = popularity,
        voteAverage = voteAverage
    )
}

fun movieFromSeed(
    seed: Int,
    id: Int = seed,
    title: String = FakeMovieDefaults.title(seed),
    overview: String = FakeMovieDefaults.overview(seed),
    releaseDate: String = FakeMovieDefaults.RELEASE_DATE,
    poster: String = FakeMovieDefaults.poster(seed),
    backdrop: String? = FakeMovieDefaults.backdrop(seed),
    originalTitle: String = FakeMovieDefaults.originalTitle(seed),
    originalLanguage: String = FakeMovieDefaults.ORIGINAL_LANGUAGE,
    popularity: Double = FakeMovieDefaults.POPULARITY,
    voteAverage: Double = FakeMovieDefaults.VOTE_AVERAGE,
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

fun tmdbRemoteMovie(
    seed: Int,
    id: Int = seed,
    title: String = FakeMovieDefaults.title(seed),
    overview: String = FakeMovieDefaults.overview(seed),
    releaseDate: String? = FakeMovieDefaults.RELEASE_DATE,
    posterPath: String? = FakeMovieDefaults.posterPath(seed),
    backdropPath: String? = FakeMovieDefaults.backdropPath(seed),
    originalTitle: String = FakeMovieDefaults.originalTitle(seed),
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

fun omdbRemoteMovie(
    seed: Int,
    title: String = FakeMovieDefaults.title(seed),
    plot: String = FakeMovieDefaults.overview(seed),
    released: String = FakeMovieDefaults.RELEASE_DATE,
    year: String = released.take(4),
    poster: String = FakeMovieDefaults.poster(seed),
    language: String = FakeMovieDefaults.ORIGINAL_LANGUAGE,
    metaScore: String = String.format("%.1f", FakeMovieDefaults.VOTE_AVERAGE),
    imdbRating: String = String.format("%.1f", FakeMovieDefaults.POPULARITY),
): OMDBRemoteMovie {
    return OMDBRemoteMovie(
        title = title,
        plot = plot,
        released = released,
        year = year,
        poster = poster,
        language = language,
        metaScore = metaScore,
        imdbRating = imdbRating
    )
}

fun tmdbRemoteResult(
    results: List<TMDBRemoteMovie>,
    page: Int = 1,
    totalPages: Int = 1,
): TMDBRemoteResult {
    return TMDBRemoteResult(
        page = page,
        results = results,
        totalPages = totalPages,
        totalResults = results.size
    )
}

