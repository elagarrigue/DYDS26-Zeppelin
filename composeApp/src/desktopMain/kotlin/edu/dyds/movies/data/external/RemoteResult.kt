package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface RemoteResult {
    fun toDomainMovieList(): List<Movie>
}
