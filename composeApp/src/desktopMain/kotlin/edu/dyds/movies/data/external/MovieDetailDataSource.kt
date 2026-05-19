package edu.dyds.movies.data.external

import edu.dyds.movies.data.external.tmdb.TMDBRemoteMovie

interface MovieDetailDataSource {
    suspend fun getMovieByTitle(title: String): TMDBRemoteMovie
}