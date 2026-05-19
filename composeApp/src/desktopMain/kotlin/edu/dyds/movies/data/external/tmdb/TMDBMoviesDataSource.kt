package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.MovieDetailDataSource
import edu.dyds.movies.data.external.PopularMoviesDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val API_KEY = "d18da1b5da16397619c688b0263cd281"

class TMDBMoviesDataSource : PopularMoviesDataSource, MovieDetailDataSource {
    private val httpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org"
                    parameters.append("api_key", API_KEY)
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }

    override suspend fun getPopularMovies(): TMDBRemoteResult =
        httpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

    override suspend fun getMovieByTitle(title: String): TMDBRemoteMovie =
        httpClient.get("/3/search/movie") {
            url { parameters.append("query", title) }
        }.body<TMDBRemoteResult>().results.first()
}