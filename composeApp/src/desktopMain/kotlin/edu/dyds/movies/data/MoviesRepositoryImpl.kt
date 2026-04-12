package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.data.local.MoviesLocalDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MoviesRepositoryImpl(
    private val tmdbHttpClient: HttpClient,
    private val moviesLocalDataSource: MoviesLocalDataSource
) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        val cachedMovies = moviesLocalDataSource.getPopularMovies()

        return if (cachedMovies.isNotEmpty()) {
            cachedMovies
        } else {
            try {
                val remoteMovies = getTMDBPopularMovies().results
                val mappedMovies = remoteMovies.map { it.toDomainMovie() }
                moviesLocalDataSource.savePopularMovies(mappedMovies)
                mappedMovies
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        return try {
            getTMDBMovieDetails(id).toDomainMovie()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

    private suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()
}
