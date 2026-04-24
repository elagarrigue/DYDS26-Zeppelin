package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.tmdb.TheMovieDB
import edu.dyds.movies.data.local.MoviesCache
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.viewmodel.MovieDetailsViewModel
import edu.dyds.movies.presentation.viewmodel.PopularMoviesViewModel

object MoviesDependencyInjector {
    private val moviesRepository = MoviesRepositoryImpl(TheMovieDB, MoviesCache)
    private val getPopularMoviesUseCase = GetPopularMoviesUseCase(moviesRepository)
    private val getMovieDetailsUseCase = GetMovieDetailsUseCase(moviesRepository)

    @Composable
    fun getPopularMoviesViewModel(): PopularMoviesViewModel {
        return viewModel { 
            PopularMoviesViewModel(
                getPopularMovies = getPopularMoviesUseCase,
            )
        }
    }

    @Composable
    fun getMovieDetailsViewModel(): MovieDetailsViewModel{
        return viewModel {
            MovieDetailsViewModel(
                getMovieDetails = getMovieDetailsUseCase,
            )
        }
    }
}