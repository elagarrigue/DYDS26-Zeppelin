package edu.dyds.movies

import edu.dyds.movies.domain.entity.Movie

private object FakeMovieDefaults {
    const val RELEASE_DATE = "2024-01-01"
    const val ORIGINAL_LANGUAGE = "en"
    const val POPULARITY = 10.0
    const val VOTE_AVERAGE = 5.0

    fun title(id: Int) = "Movie $id"
    fun overview(id: Int) = "Overview $id"
    fun originalTitle(id: Int) = "Original $id"
    fun poster(id: Int) = "poster-$id"
    fun backdrop(id: Int) = "backdrop-$id"
    fun posterPath(id: Int) = "/poster-$id.png"
    fun backdropPath(id: Int) = "/backdrop-$id.png"
}

fun movie(
    id: Int,
    title: String = FakeMovieDefaults.title(id),
    overview: String = FakeMovieDefaults.overview(id),
    releaseDate: String = FakeMovieDefaults.RELEASE_DATE,
    poster: String = FakeMovieDefaults.poster(id),
    backdrop: String? = FakeMovieDefaults.backdrop(id),
    originalTitle: String = FakeMovieDefaults.originalTitle(id),
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
