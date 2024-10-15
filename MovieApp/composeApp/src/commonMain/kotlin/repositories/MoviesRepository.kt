package repositories

import domain.model.FavoriteMovieDomain
import domain.model.Movie
import domain.model.MovieCredits
import domain.model.MovieDetails
import domain.model.MovieReviewFilter
import domain.model.MovieReviews
import domain.model.PopularMovieFilter
import domain.model.TrendingMovieFilter
import domain.model.WatchNowFilter
import domain.model.toFavoriteMovie
import domain.model.toMovie
import domain.model.toMovieDetails
import domain.model.toMovieReviews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import source.local.LocalDataSource
import source.remote.RemoteDataSource

internal interface MoviesRepository {

    suspend fun getMovieById(movieId: String): Flow<MovieDetails>

    suspend fun getTvById(movieId: String): Flow<MovieDetails>

    suspend fun getMovieCreditsById(movieId: String): Flow<MovieCredits>

    suspend fun getRecommendedMoviesById(movieId: String): Flow<List<Movie>>

    suspend fun getMovieReviewsById(
        movieId: String,
        movieReviewFilter: MovieReviewFilter
    ): Flow<List<MovieReviews>>

    suspend fun getTvCreditsById(movieId: String): Flow<MovieCredits>

    suspend fun getRecommendedTvsById(movieId: String): Flow<List<Movie>>

    suspend fun getTvReviewsById(
        movieId: String,
        movieReviewFilter: MovieReviewFilter
    ): Flow<List<MovieReviews>>

    suspend fun getPopularMovies(popularMovieFilter: PopularMovieFilter): Flow<List<Movie>>

    suspend fun getTrendingMovies(trendingMovieFilter: TrendingMovieFilter): Flow<List<Movie>>

    suspend fun getWatchNow(watchNowFilter: WatchNowFilter): Flow<List<Movie>>

    fun getFavoriteMovies(): Flow<List<FavoriteMovieDomain>>

    suspend fun insertFavoriteMovie(movieId: Long, posterPath: String, movieType: String)

    suspend fun removeFavoriteMovie(movieId: Long)

    suspend fun getFavoriteMovieById(movieId: String): Flow<MovieDetails>

    suspend fun getFavoritePopularMovies(popularMovieFilter: PopularMovieFilter): Flow<List<Movie>>

    suspend fun getFavoriteTrendingMovies(trendingMovieFilter: TrendingMovieFilter): Flow<List<Movie>>

    suspend fun getFavoriteWatchNow(watchNowFilter: WatchNowFilter): Flow<List<Movie>>

}

internal class MoviesRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : MoviesRepository {

    override suspend fun getMovieById(movieId: String): Flow<MovieDetails> {
        return flow { emit(remoteDataSource.getMovieById(movieId).toMovieDetails()) }
    }

    override suspend fun getTvById(movieId: String): Flow<MovieDetails> =
        flow { emit(remoteDataSource.getTvById(movieId).toMovieDetails()) }

    override suspend fun getMovieCreditsById(movieId: String): Flow<MovieCredits> =
        flow { emit(remoteDataSource.getMovieCreditsById(movieId).toMovieDetails()) }

    override suspend fun getRecommendedMoviesById(movieId: String): Flow<List<Movie>> =
        flowOf(remoteDataSource.getRecommendedMoviesById(movieId).results.map {
            it.toMovie()
        })

    override suspend fun getMovieReviewsById(
        movieId: String,
        movieReviewFilter: MovieReviewFilter
    ): Flow<List<MovieReviews>> {
        return when (movieReviewFilter) {
            MovieReviewFilter.REVIEWS -> flowOf(remoteDataSource.getMovieReviewsById(movieId).results.map {
                it.toMovieReviews()
            })

            MovieReviewFilter.DISCUSSIONS -> flowOf(remoteDataSource.getMovieReviewsById(movieId).results.map {
                it.toMovieReviews()
            })
        }
    }

    override suspend fun getTvCreditsById(movieId: String): Flow<MovieCredits> =
        flow { emit(remoteDataSource.getTvCreditsById(movieId).toMovieDetails()) }

    override suspend fun getRecommendedTvsById(movieId: String): Flow<List<Movie>> =
        flowOf(remoteDataSource.getRecommendedTvsById(movieId).results.map {
            it.toMovie()
        })

    override suspend fun getTvReviewsById(
        movieId: String,
        movieReviewFilter: MovieReviewFilter
    ): Flow<List<MovieReviews>> {
        return when (movieReviewFilter) {
            MovieReviewFilter.REVIEWS -> flowOf(remoteDataSource.getTvReviewsById(movieId).results.map {
                it.toMovieReviews()
            })

            MovieReviewFilter.DISCUSSIONS -> flowOf(remoteDataSource.getTvReviewsById(movieId).results.map {
                it.toMovieReviews()
            })
        }
    }

    override suspend fun getPopularMovies(popularMovieFilter: PopularMovieFilter): Flow<List<Movie>> {
        return when (popularMovieFilter) {
            PopularMovieFilter.MOVIES -> flowOf(remoteDataSource.getPopularMovies().results.map {
                it.toMovie()
            })

            PopularMovieFilter.TV -> flowOf(remoteDataSource.getPopularTV().results.map {
                it.toMovie()
            })
        }
    }

    override suspend fun getTrendingMovies(trendingMovieFilter: TrendingMovieFilter): Flow<List<Movie>> {
        return when (trendingMovieFilter) {
            TrendingMovieFilter.DAY -> flowOf(remoteDataSource.getDailyTrendingMovies().results.map {
                it.toMovie()
            })

            TrendingMovieFilter.WEEK -> flowOf(remoteDataSource.getWeeklyTrendingMovies().results.map {
                it.toMovie()
            })
        }

    }

    override suspend fun getWatchNow(watchNowFilter: WatchNowFilter): Flow<List<Movie>> {
        return when (watchNowFilter) {
            WatchNowFilter.MOVIES -> flowOf(remoteDataSource.getWatchNowMovie().results.map {
                it.toMovie()
            })

            WatchNowFilter.TV -> flowOf(remoteDataSource.getWatchNowTV().results.map {
                it.toMovie()
            })
        }

    }

    override fun getFavoriteMovies(): Flow<List<FavoriteMovieDomain>> {
        return localDataSource.getAllFavoriteMovies().map { favoriteMovies ->
            favoriteMovies.map { it.toFavoriteMovie() }
        }
    }

    override suspend fun insertFavoriteMovie(movieId: Long, posterPath: String, movieType: String) =
        localDataSource.insertFavoriteMovieById(movieId, posterPath, movieType)

    override suspend fun removeFavoriteMovie(movieId: Long) =
        localDataSource.deleteFavoriteMovieById(movieId)

    override suspend fun getFavoriteMovieById(movieId: String): Flow<MovieDetails> {
        return combine(
            getMovieById(movieId),
            getFavoriteMovies()
        ) { movieDetails, favoriteMovies ->
            val isFavorite = favoriteMovies.any { it.movieId == movieId.toInt() }

            val updatedMovieDetails = movieDetails.copy(isFavorite = isFavorite)

            return@combine updatedMovieDetails
        }
    }

    override suspend fun getFavoritePopularMovies(popularMovieFilter: PopularMovieFilter): Flow<List<Movie>> {
        return combine(
            getPopularMovies(popularMovieFilter),
            getFavoriteMovies()
        ) { popularMovies, favoriteMovies ->

            val updatedMovies = popularMovies.map { popMovie ->
                popMovie.copy(isFavorite = favoriteMovies.any { favMovie -> popMovie.id == favMovie.movieId })
            }

            return@combine updatedMovies
        }
    }

    override suspend fun getFavoriteTrendingMovies(trendingMovieFilter: TrendingMovieFilter): Flow<List<Movie>> {
        return combine(
            getTrendingMovies(trendingMovieFilter),
            getFavoriteMovies()
        ) { trendingMovies, favoriteMovies ->

            val updatedTrendingMovies = trendingMovies.map { trendMovie ->
                trendMovie.copy(isFavorite = favoriteMovies.any { favMovie -> trendMovie.id == favMovie.movieId })
            }

            return@combine updatedTrendingMovies
        }
    }

    override suspend fun getFavoriteWatchNow(watchNowFilter: WatchNowFilter): Flow<List<Movie>> {
        return combine(
            getWatchNow(watchNowFilter),
            getFavoriteMovies()
        ) { watchNow, favoriteMovies ->

            val updatedWatchNow = watchNow.map { movie ->
                movie.copy(isFavorite = favoriteMovies.any { favMovie -> movie.id == favMovie.movieId })
            }

            return@combine updatedWatchNow
        }
    }

}
