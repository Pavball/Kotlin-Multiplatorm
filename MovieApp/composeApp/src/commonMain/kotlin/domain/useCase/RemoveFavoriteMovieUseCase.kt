package domain.useCase

import repositories.MoviesRepository

internal interface RemoveFavoriteMovieUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: Long)

}

    internal class RemoveFavoriteMovie(private val moviesRepository: MoviesRepository) :
        RemoveFavoriteMovieUseCase {
        override suspend fun invoke(movieId: Long): Unit = moviesRepository.removeFavoriteMovie(movieId)
    }

