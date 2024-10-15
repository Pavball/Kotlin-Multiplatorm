package source.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import source.remote.model.ApiMovie
import source.remote.model.ApiMovieCredits
import source.remote.model.ApiMovieDetails
import source.remote.model.ApiMovieReviews
import source.remote.model.ApiTv
import source.remote.model.ApiTvDetails
import source.remote.model.PageResponse

const val POPULAR_MOVIES = "$BASE_URL_MOVIE/popular"
const val POPULAR_TV = "$BASE_URL_TV/popular"

const val TRENDING_MOVIES_DAY = "$BASE_URL_TRENDING_MOVIE/day"
const val TRENDING_MOVIES_WEEK = "$BASE_URL_TRENDING_MOVIE/week"

const val WATCH_NOW_MOVIE  = BASE_URL_WATCH_NOW_MOVIE
const val WATCH_NOW_TV  = BASE_URL_WATCH_NOW_TV

const val API_URL = BASE_URL_API

const val GET_MOVIE_BY_ID = "$BASE_URL_MOVIE/"
const val GET_TV_BY_ID = "$BASE_URL_TV/"

const val PAGE = "&page=2"

internal interface RemoteDataSource {
    suspend fun getPopularMovies(): PageResponse<ApiMovie>
    suspend fun getPopularTV(): PageResponse<ApiTv>
    suspend fun getDailyTrendingMovies(): PageResponse<ApiMovie>
    suspend fun getWeeklyTrendingMovies(): PageResponse<ApiMovie>
    suspend fun getWatchNowMovie(): PageResponse<ApiMovie>
    suspend fun getWatchNowTV(): PageResponse<ApiTv>
    suspend fun getMovieById(movieId: String): ApiMovieDetails
    suspend fun getTvById(movieId: String): ApiTvDetails
    suspend fun getMovieCreditsById(movieId: String): ApiMovieCredits
    suspend fun getMovieReviewsById(movieId: String): PageResponse<ApiMovieReviews>
    suspend fun getRecommendedMoviesById(movieId: String): PageResponse<ApiMovie>
    suspend fun getTvCreditsById(movieId: String): ApiMovieCredits
    suspend fun getTvReviewsById(movieId: String): PageResponse<ApiMovieReviews>
    suspend fun getRecommendedTvsById(movieId: String): PageResponse<ApiTv>
}


internal class RemoteDataSourceImpl(
    private val httpClient: HttpClient,
    ) : RemoteDataSource {

    override suspend fun getPopularMovies(): PageResponse<ApiMovie> =
        httpClient.getWithAuth(url = POPULAR_MOVIES + API_URL + ApiKey() + PAGE)

    override suspend fun getPopularTV(): PageResponse<ApiTv> =
        httpClient.getWithAuth(url = POPULAR_TV + API_URL + ApiKey())

    override suspend fun getDailyTrendingMovies(): PageResponse<ApiMovie> =
        httpClient.getWithAuth(url = TRENDING_MOVIES_DAY + API_URL + ApiKey())

    override suspend fun getWeeklyTrendingMovies(): PageResponse<ApiMovie> =
        httpClient.getWithAuth(url = TRENDING_MOVIES_WEEK + API_URL + ApiKey())

    override suspend fun getWatchNowMovie(): PageResponse<ApiMovie> =
        httpClient.getWithAuth(url = WATCH_NOW_MOVIE + API_URL + ApiKey())

    override suspend fun getWatchNowTV(): PageResponse<ApiTv> =
        httpClient.getWithAuth(url = WATCH_NOW_TV + API_URL + ApiKey())

    override suspend fun getMovieById(movieId: String): ApiMovieDetails =
        httpClient.getWithAuth(url = GET_MOVIE_BY_ID + movieId + API_URL + ApiKey())

    override suspend fun getTvById(movieId: String): ApiTvDetails =
        httpClient.getWithAuth(url = GET_TV_BY_ID + movieId + API_URL + ApiKey())

    override suspend fun getMovieCreditsById(movieId: String): ApiMovieCredits =
        httpClient.getWithAuth(url = GET_MOVIE_BY_ID + movieId + "/credits" + API_URL + ApiKey())

    override suspend fun getMovieReviewsById(movieId: String): PageResponse<ApiMovieReviews> =
        httpClient.getWithAuth(url = GET_MOVIE_BY_ID + movieId + "/reviews" + API_URL + ApiKey())

    override suspend fun getRecommendedMoviesById(movieId: String): PageResponse<ApiMovie> =
        httpClient.getWithAuth(url = GET_MOVIE_BY_ID + movieId + "/recommendations" + API_URL + ApiKey())

    override suspend fun getTvCreditsById(movieId: String): ApiMovieCredits =
        httpClient.getWithAuth(url = GET_TV_BY_ID + movieId + "/credits" + API_URL + ApiKey())

    override suspend fun getTvReviewsById(movieId: String): PageResponse<ApiMovieReviews> =
        httpClient.getWithAuth(url = GET_TV_BY_ID + movieId + "/reviews" + API_URL + ApiKey())

    override suspend fun getRecommendedTvsById(movieId: String): PageResponse<ApiTv> =
        httpClient.getWithAuth(url = GET_TV_BY_ID + movieId + "/recommendations" + API_URL + ApiKey())


}

private suspend inline fun <reified T> HttpClient.getWithAuth(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T =
    get(urlString = url) {
        block()
    }.body<T>()
