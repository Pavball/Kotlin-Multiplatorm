package domain.useCase

import domain.model.MovieCredits
import kotlinx.coroutines.flow.Flow
import repositories.MoviesRepository

internal interface GetTvCreditsByIdUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: String): Flow<MovieCredits>
}

internal class GetTvCreditsById(private val moviesRepository: MoviesRepository) :
    GetTvCreditsByIdUseCase {
    override suspend fun invoke(movieId: String): Flow<MovieCredits> = moviesRepository.getTvCreditsById(movieId)
}
