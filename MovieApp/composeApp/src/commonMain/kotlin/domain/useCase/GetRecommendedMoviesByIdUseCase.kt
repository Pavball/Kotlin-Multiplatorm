package domain.useCase

import domain.model.Movie
import kotlinx.coroutines.flow.Flow
import repositories.MoviesRepository

internal interface GetRecommendedMoviesByIdUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: String): Flow<List<Movie>>
}

internal class GetRecommendedMoviesById(private val moviesRepository: MoviesRepository) :
    GetRecommendedMoviesByIdUseCase {
    override suspend fun invoke(movieId: String): Flow<List<Movie>> = moviesRepository.getRecommendedMoviesById(movieId)
}
