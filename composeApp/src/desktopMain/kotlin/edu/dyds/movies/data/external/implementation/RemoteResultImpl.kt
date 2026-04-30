package edu.dyds.movies.data.external.implementation

import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteResultImpl(
    val page: Int,
    val results: List<RemoteMovieImpl>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int,
): RemoteResult {
    override fun toDomainMovieList(): List<Movie> {
        return results.map { it.toDomainMovie() }
    }
}
