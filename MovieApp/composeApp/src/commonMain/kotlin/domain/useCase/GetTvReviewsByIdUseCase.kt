package domain.useCase

import domain.model.MovieReviewFilter
import domain.model.MovieReviews
import kotlinx.coroutines.flow.Flow
import repositories.MoviesRepository

internal interface GetTvReviewsByIdUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: String, movieReviewFilter: MovieReviewFilter): Flow<List<MovieReviews>>
}

internal class GetTvReviewsById(private val moviesRepository: MoviesRepository) :
    GetTvReviewsByIdUseCase {
    override suspend fun invoke(movieId: String, movieReviewFilter: MovieReviewFilter): Flow<List<MovieReviews>> = moviesRepository.getTvReviewsById(movieId, movieReviewFilter)

}
