package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OMDBRemoteMovie(
    @SerialName(value = "Title") val title: String,
    @SerialName(value = "Plot") val plot: String,
    @SerialName(value = "Released") val released: String,
    @SerialName(value = "Year") val year: String,
    @SerialName(value = "Poster") val poster: String,
    @SerialName(value = "Language") val language: String,
    @SerialName(value = "Metascore") val metaScore: String,
    @SerialName(value = "imdbRating") val imdbRating: String,
) {
    fun toDomainMovie(): Movie {
        val release = if (released.isNotEmpty() && released != "N/A") released else year
        val voteAverage = if (metaScore.isNotEmpty() && metaScore != "N/A") metaScore.toDouble() else 0.0
        val popularity = if (imdbRating.isNotEmpty() && imdbRating != "N/A") imdbRating.toDouble() else 0.0

        return Movie(
            id = title.hashCode(),
            title = title,
            overview = plot,
            releaseDate = release,
            poster = poster,
            backdrop = poster,
            originalTitle = title,
            originalLanguage = language,
            popularity = popularity,
            voteAverage = voteAverage
        )
    }
}

