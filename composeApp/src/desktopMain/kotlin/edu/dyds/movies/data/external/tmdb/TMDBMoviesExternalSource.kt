package edu.dyds.movies.data.external.tmdb

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TMDB_API_KEY = "d18da1b5da16397619c688b0263cd281"

internal interface TMDBMoviesExternalSource {
    suspend fun getMovieDetailResult(title: String): TMDBRemoteResult

    suspend fun getPopularMoviesResult(): TMDBRemoteResult
}

internal class TMDBMoviesExternalSourceImpl : TMDBMoviesExternalSource {
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
                    parameters.append("api_key", TMDB_API_KEY)
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }

    override suspend fun getMovieDetailResult(title: String): TMDBRemoteResult =
        httpClient.get("/3/search/movie") {
            url { parameters.append("query", title) }
        }.body()

    override suspend fun getPopularMoviesResult(): TMDBRemoteResult =
        httpClient.get("/3/discover/movie?sort_by=popularity.desc").body()
}