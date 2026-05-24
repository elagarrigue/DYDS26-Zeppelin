package edu.dyds.movies.data.external.omdb

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
)

