package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.tmdb.TMDB
import edu.dyds.movies.data.local.InMemoryMovies
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.viewmodel.MoviesViewModel


object MoviesDependencyInjector {
    private val moviesRepository = MoviesRepositoryImpl(TMDB, InMemoryMovies)
    private val getPopularMoviesUseCase = GetPopularMoviesUseCase(moviesRepository)
    private val getMovieDetailsUseCase = GetMovieDetailsUseCase(moviesRepository)

    @Composable
    fun getMoviesViewModel(): MoviesViewModel {
        return viewModel { 
            MoviesViewModel(
                getPopularMoviesUseCase = getPopularMoviesUseCase,
                getMovieDetailsUseCase = getMovieDetailsUseCase
            ) 
        }
    }
}