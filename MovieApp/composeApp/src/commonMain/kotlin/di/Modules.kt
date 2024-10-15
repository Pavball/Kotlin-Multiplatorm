package di

import domain.useCase.AddFavoriteMovieUseCase
import domain.useCase.GetFavoriteMovies
import domain.useCase.GetFavoriteMoviesUseCase
import domain.useCase.GetFavoritePopularMovies
import domain.useCase.GetFavoriteTrendingMovies
import domain.useCase.GetFavoriteWatchNow
import domain.useCase.GetMovieById
import domain.useCase.GetMovieByIdUseCase
import domain.useCase.GetMovieCreditsById
import domain.useCase.GetMovieCreditsByIdUseCase
import domain.useCase.GetMovieReviewsById
import domain.useCase.GetMovieReviewsByIdUseCase
import domain.useCase.GetPopularMoviesUseCase
import domain.useCase.GetRecommendedMoviesById
import domain.useCase.GetRecommendedMoviesByIdUseCase
import domain.useCase.GetRecommendedTvsById
import domain.useCase.GetRecommendedTvsByIdUseCase
import domain.useCase.GetTrendingMoviesUseCase
import domain.useCase.GetTvById
import domain.useCase.GetTvByIdUseCase
import domain.useCase.GetTvCreditsById
import domain.useCase.GetTvCreditsByIdUseCase
import domain.useCase.GetTvReviewsById
import domain.useCase.GetTvReviewsByIdUseCase
import domain.useCase.GetWatchNowUseCase
import domain.useCase.InsertFavoriteMovie
import domain.useCase.RemoveFavoriteMovie
import domain.useCase.RemoveFavoriteMovieUseCase
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import repositories.MoviesRepository
import repositories.MoviesRepositoryImpl
import source.local.LocalDataSource
import source.local.LocalDataSourceImpl
import ui.viewmodels.DetailScreenViewModel
import ui.viewmodels.DetailScreenViewModelImpl
import ui.viewmodels.FavoriteScreenViewModel
import ui.viewmodels.FavoriteScreenViewModelImpl
import ui.viewmodels.HomeScreenViewModel
import ui.viewmodels.HomeScreenViewModelImpl

expect val platformModule: Module

internal val sharedModule = module {
    single<LocalDataSource> { LocalDataSourceImpl(get()) }

    single<MoviesRepository> { MoviesRepositoryImpl(get(), get()) }

    viewModel<HomeScreenViewModel> {
        HomeScreenViewModelImpl(
            getFavoritePopularMovies = get(),
            getFavoriteWatchNow = get(),
            getFavoriteTrendingMovies = get(),
            insertFavoriteMovie = get(),
            removeFavoriteMovie = get()
        )
    }

    viewModel<FavoriteScreenViewModel> {
        FavoriteScreenViewModelImpl(
            getFavoriteMovies = get(),
            removeFavoriteMovie = get()
        )
    }

    viewModel<DetailScreenViewModel> { (movieId: String, movieType: String) ->
        DetailScreenViewModelImpl(
            movieId = movieId,
            movieType = movieType,
            getMovieById = get(),
            getMovieCreditsById = get(),
            getMovieReviewsById = get(),
            getRecommendedMoviesById = get(),
            getTvById = get(),
            getTvCreditsById = get(),
            getTvReviewsById = get(),
            getRecommendedTvsById = get(),
            insertFavoriteMovie = get(),
            removeFavoriteMovie = get(),
        )
    }

    //HomeScreenViewModel Section
    single<GetPopularMoviesUseCase> {
        GetFavoritePopularMovies(
            moviesRepository = get(),
        )
    }

    single<GetWatchNowUseCase> {
        GetFavoriteWatchNow(
            moviesRepository = get(),
        )
    }

    single<GetTrendingMoviesUseCase> {
        GetFavoriteTrendingMovies(
            moviesRepository = get(),
        )
    }

    single<AddFavoriteMovieUseCase> {
        InsertFavoriteMovie(
            moviesRepository = get(),
        )
    }

    single<RemoveFavoriteMovieUseCase> {
        RemoveFavoriteMovie(
            moviesRepository = get(),
        )
    }

    //FavoriteScreenViewModel Section
    single<GetFavoriteMoviesUseCase> {
        GetFavoriteMovies(
            moviesRepository = get(),
        )
    }

    //DetailsScreenViewModel Section
    single<GetMovieByIdUseCase> {
        GetMovieById(
            moviesRepository = get(),
        )
    }

    single<GetMovieCreditsByIdUseCase> {
        GetMovieCreditsById(
            moviesRepository = get(),
        )
    }

    single<GetMovieReviewsByIdUseCase> {
        GetMovieReviewsById(
            moviesRepository = get(),
        )
    }

    single<GetRecommendedMoviesByIdUseCase> {
        GetRecommendedMoviesById(
            moviesRepository = get(),
        )
    }

    single<GetTvByIdUseCase> {
        GetTvById(
            moviesRepository = get(),
        )
    }

    single<GetTvCreditsByIdUseCase> {
        GetTvCreditsById(
            moviesRepository = get(),
        )
    }

    single<GetTvReviewsByIdUseCase> {
        GetTvReviewsById(
            moviesRepository = get(),
        )
    }

    single<GetRecommendedTvsByIdUseCase> {
        GetRecommendedTvsById(
            moviesRepository = get(),
        )
    }

}