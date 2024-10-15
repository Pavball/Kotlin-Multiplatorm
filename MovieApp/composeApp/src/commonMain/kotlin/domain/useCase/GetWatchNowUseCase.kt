package domain.useCase

import kotlinx.coroutines.flow.Flow
import domain.model.Movie
import domain.model.WatchNowFilter
import repositories.MoviesRepository

internal interface GetWatchNowUseCase /*@Inject constructor*/ {

    suspend operator fun invoke(watchNowFilter: WatchNowFilter): Flow<List<Movie>>

}

internal class GetFavoriteWatchNow(private val moviesRepository: MoviesRepository) :
    GetWatchNowUseCase {
    override suspend fun invoke(watchNowFilter: WatchNowFilter): Flow<List<Movie>> = moviesRepository.getFavoriteWatchNow(watchNowFilter)
}
