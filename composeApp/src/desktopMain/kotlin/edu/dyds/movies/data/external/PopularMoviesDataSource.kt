package edu.dyds.movies.data.external

import edu.dyds.movies.data.external.tmdb.TMDBRemoteResult

interface PopularMoviesDataSource {
    suspend fun getPopularMovies(): TMDBRemoteResult
}