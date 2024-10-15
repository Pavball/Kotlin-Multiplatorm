package domain.useCase

import kotlinx.coroutines.flow.Flow
import domain.model.Movie
import domain.model.PopularMovieFilter
import repositories.MoviesRepository

internal interface GetPopularMoviesUseCase /*@Inject constructor*/ {
    suspend operator fun invoke(popularMovieFilter: PopularMovieFilter): Flow<List<Movie>>
}

internal class GetFavoritePopularMovies(
    private val moviesRepository: MoviesRepository
) : GetPopularMoviesUseCase {
    override suspend fun invoke(popularMovieFilter: PopularMovieFilter): Flow<List<Movie>> =
        moviesRepository.getFavoritePopularMovies(popularMovieFilter)
}
