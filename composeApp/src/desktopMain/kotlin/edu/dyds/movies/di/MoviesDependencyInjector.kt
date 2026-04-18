package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.tmdb.TMDB
import edu.dyds.movies.data.local.InMemoryMovies
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.viewmodel.MovieDetailsViewModel
import edu.dyds.movies.presentation.viewmodel.PopularMoviesViewModel

object MoviesDependencyInjector {
    private val moviesRepository = MoviesRepositoryImpl(TMDB, InMemoryMovies)

    @Composable
    fun getPopularMoviesViewModel(): PopularMoviesViewModel {
        return viewModel { 
            PopularMoviesViewModel(
                getPopularMovies = GetPopularMoviesUseCase(moviesRepository),
            )
        }
    }

    @Composable
    fun getMovieDetailsViewModel(): MovieDetailsViewModel{
        return viewModel {
            MovieDetailsViewModel(
                getMovieDetails = GetMovieDetailsUseCase(moviesRepository),
            )
        }
    }
}