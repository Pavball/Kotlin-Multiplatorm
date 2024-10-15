package domain.useCase

import kotlinx.coroutines.flow.Flow
import domain.model.MovieDetails
import repositories.MoviesRepository

internal interface GetMovieByIdUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: String): Flow<MovieDetails>
}

internal class GetMovieById(private val moviesRepository: MoviesRepository) : GetMovieByIdUseCase {
    override suspend fun invoke(movieId: String): Flow<MovieDetails> = moviesRepository.getFavoriteMovieById(movieId)
}