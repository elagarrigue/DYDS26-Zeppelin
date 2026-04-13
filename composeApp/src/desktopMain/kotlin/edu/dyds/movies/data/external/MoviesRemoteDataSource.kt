package edu.dyds.movies.data.external

interface MoviesRemoteDataSource {
    suspend fun getPopularMovies(): RemoteResult
    suspend fun getMovieDetails(id: Int): RemoteMovie
}