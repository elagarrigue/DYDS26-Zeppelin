package edu.dyds.movies.presentation

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.CompletableDeferred

class FakeGetPopularMoviesUseCase(
    private val popularMovies: List<QualifiedMovie> = emptyList(),
    private val returnSignal: CompletableDeferred<Unit>? = null,
) : GetPopularMoviesUseCase {
    var getPopularMoviesCalls = 0

    override suspend fun invoke(): List<QualifiedMovie> {
        getPopularMoviesCalls += 1
        returnSignal?.await()
        return popularMovies
    }
}

class FakeGetMovieDetailsUseCase(
    private val movie: Movie? = null,
    private val returnSignal: CompletableDeferred<Unit>? = null,
) : GetMovieDetailsUseCase {
    var getMovieDetailsCalls = 0
    var lastRequestedId: Int? = null

    override suspend fun invoke(id: Int): Movie? {
        getMovieDetailsCalls += 1
        lastRequestedId = id
        returnSignal?.await()
        return movie
    }
}


