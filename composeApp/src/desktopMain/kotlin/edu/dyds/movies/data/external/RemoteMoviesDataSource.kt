package edu.dyds.movies.data.external

interface RemoteMoviesDataSource {
    suspend fun getPopularMovies(): RemoteResult
    suspend fun getMovieByTitle(title: String): RemoteMovie
}