package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.broker.MovieDetailExternalSourceBroker
import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.data.external.omdb.proxy.OMDBMoviesProxy
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.proxy.TMDBMoviesProxy
import edu.dyds.movies.data.local.MoviesLocalSourceImpl
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCaseImpl
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCaseImpl
import edu.dyds.movies.presentation.detail.MovieDetailsViewModel
import edu.dyds.movies.presentation.home.PopularMoviesViewModel

object MoviesDependencyInjector {
    private val tmdbMoviesExternalSource = TMDBMoviesExternalSource()
    private val omdbMoviesExternalSource = OMDBMoviesExternalSource()
    private val tmdbMoviesProxy = TMDBMoviesProxy(tmdbMoviesExternalSource)
    private val omdbMoviesProxy = OMDBMoviesProxy(omdbMoviesExternalSource)
    private val movieDetailExternalSourceBroker = MovieDetailExternalSourceBroker(tmdbMoviesProxy, omdbMoviesProxy)
    private val moviesRepository = MoviesRepositoryImpl(tmdbMoviesProxy, movieDetailExternalSourceBroker, MoviesLocalSourceImpl())
    private val getPopularMoviesUseCase = GetPopularMoviesUseCaseImpl(moviesRepository)
    private val getMovieDetailsUseCase = GetMovieDetailsUseCaseImpl(moviesRepository)

    @Composable
    fun getPopularMoviesViewModel(): PopularMoviesViewModel {
        return viewModel { 
            PopularMoviesViewModel(
                getPopularMoviesUseCase = getPopularMoviesUseCase,
            )
        }
    }

    @Composable
    fun getMovieDetailsViewModel(): MovieDetailsViewModel{
        return viewModel {
            MovieDetailsViewModel(
                getMovieDetailsUseCase = getMovieDetailsUseCase,
            )
        }
    }
}