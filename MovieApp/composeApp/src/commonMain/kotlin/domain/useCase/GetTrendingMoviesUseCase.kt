package domain.useCase

import kotlinx.coroutines.flow.Flow
import domain.model.Movie
import domain.model.TrendingMovieFilter
import repositories.MoviesRepository

internal interface GetTrendingMoviesUseCase {

    suspend operator fun invoke(trendingMovieFilter: TrendingMovieFilter): Flow<List<Movie>>
}

internal class GetFavoriteTrendingMovies(val moviesRepository: MoviesRepository) :
    GetTrendingMoviesUseCase {
    override suspend fun invoke(trendingMovieFilter: TrendingMovieFilter): Flow<List<Movie>> = moviesRepository.getFavoriteTrendingMovies(trendingMovieFilter)
}