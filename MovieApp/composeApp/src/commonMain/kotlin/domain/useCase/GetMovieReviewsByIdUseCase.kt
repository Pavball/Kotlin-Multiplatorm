package domain.useCase

import domain.model.MovieReviewFilter
import domain.model.MovieReviews
import kotlinx.coroutines.flow.Flow
import repositories.MoviesRepository

internal interface GetMovieReviewsByIdUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(movieId: String, movieReviewFilter: MovieReviewFilter): Flow<List<MovieReviews>>
}

internal class GetMovieReviewsById(private val moviesRepository: MoviesRepository) :
    GetMovieReviewsByIdUseCase {
    override suspend fun invoke(movieId: String, movieReviewFilter: MovieReviewFilter): Flow<List<MovieReviews>> = moviesRepository.getMovieReviewsById(movieId, movieReviewFilter)
}
