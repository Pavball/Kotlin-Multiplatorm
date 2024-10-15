package domain.useCase

import kotlinx.coroutines.flow.Flow
import domain.model.FavoriteMovieDomain
import repositories.MoviesRepository

internal interface GetFavoriteMoviesUseCase /*@Inject constructor*/ {

    operator fun invoke(): Flow<List<FavoriteMovieDomain>>
}

internal class GetFavoriteMovies(private val moviesRepository: MoviesRepository) :
    GetFavoriteMoviesUseCase {
    override fun invoke(): Flow<List<FavoriteMovieDomain>> = moviesRepository.getFavoriteMovies()

}
