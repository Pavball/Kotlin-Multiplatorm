package domain.useCase

import kotlinx.coroutines.flow.Flow
import domain.model.MovieDetails
import repositories.MoviesRepository

internal interface GetTvByIdUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: String): Flow<MovieDetails>
}

internal class GetTvById(private val moviesRepository: MoviesRepository) : GetTvByIdUseCase {
    override suspend fun invoke(movieId: String): Flow<MovieDetails> = moviesRepository.getTvById(movieId)
}