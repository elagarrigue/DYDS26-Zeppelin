package edu.dyds.movies.presentation

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase

class FakeGetPopularMoviesUseCase(
    private val popularMovies: List<QualifiedMovie> = emptyList(),
) : GetPopularMoviesUseCase {
    var getPopularMoviesCalls = 0

    override suspend fun invoke(): List<QualifiedMovie> {
        getPopularMoviesCalls += 1
        return popularMovies
    }
}

class FakeGetMovieDetailsUseCase(
    private val movie: Movie? = null,
) : GetMovieDetailsUseCase {
    var getMovieDetailsCalls = 0
    var lastRequestedTitle: String? = null

    override suspend fun invoke(title: String): Movie? {
        getMovieDetailsCalls += 1
        lastRequestedTitle = title
        return movie
    }
}


