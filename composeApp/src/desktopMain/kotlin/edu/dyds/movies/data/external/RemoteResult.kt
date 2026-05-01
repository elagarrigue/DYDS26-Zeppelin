package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteResult(
    val page: Int,
    val results: List<RemoteMovie>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int,
) {
    fun toDomainMovieList(): List<Movie> {
        return results.map { it.toDomainMovie() }
    }
}