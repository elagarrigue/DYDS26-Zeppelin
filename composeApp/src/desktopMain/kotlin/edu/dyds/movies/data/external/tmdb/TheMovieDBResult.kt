package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TheMovieDBResult(
    val page: Int,
    val results: List<TheMovieDBMovie>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int,
): RemoteResult {
    override fun toDomainMovieList(): List<Movie> {
        return results.map { it.toDomainMovie() }
    }
}
