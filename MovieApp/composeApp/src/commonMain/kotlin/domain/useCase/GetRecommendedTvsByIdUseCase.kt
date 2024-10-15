package domain.useCase

import domain.model.Movie
import kotlinx.coroutines.flow.Flow
import repositories.MoviesRepository

internal interface GetRecommendedTvsByIdUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: String): Flow<List<Movie>>
}

internal class GetRecommendedTvsById(private val moviesRepository: MoviesRepository) :
    GetRecommendedTvsByIdUseCase {
    override suspend fun invoke(movieId: String): Flow<List<Movie>> = moviesRepository.getRecommendedTvsById(movieId)
}
