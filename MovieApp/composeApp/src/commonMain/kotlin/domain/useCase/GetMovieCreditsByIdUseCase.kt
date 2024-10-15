package domain.useCase

import domain.model.MovieCredits
import kotlinx.coroutines.flow.Flow
import repositories.MoviesRepository

internal interface GetMovieCreditsByIdUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: String): Flow<MovieCredits>
}

internal class GetMovieCreditsById(private val moviesRepository: MoviesRepository) :
    GetMovieCreditsByIdUseCase {
    override suspend fun invoke(movieId: String): Flow<MovieCredits> = moviesRepository.getMovieCreditsById(movieId)
}
