package domain.useCase

import repositories.MoviesRepository

internal interface AddFavoriteMovieUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: Long, posterPath: String, movieType: String)

}

internal class InsertFavoriteMovie(private val moviesRepository: MoviesRepository) :
    AddFavoriteMovieUseCase {
    override suspend fun invoke(movieId: Long, posterPath: String, movieType: String): Unit = moviesRepository.insertFavoriteMovie(movieId, posterPath, movieType)
}
