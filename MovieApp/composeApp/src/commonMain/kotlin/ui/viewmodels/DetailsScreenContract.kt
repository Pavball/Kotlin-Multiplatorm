@file:OptIn(ExperimentalCoroutinesApi::class)

package ui.viewmodels

import domain.model.Movie
import domain.model.MovieCredits
import domain.model.MovieDetails
import domain.model.MovieReviewFilter
import domain.model.MovieReviews
import domain.useCase.AddFavoriteMovieUseCase
import domain.useCase.GetMovieByIdUseCase
import domain.useCase.GetMovieCreditsByIdUseCase
import domain.useCase.GetMovieReviewsByIdUseCase
import domain.useCase.GetRecommendedMoviesByIdUseCase
import domain.useCase.GetRecommendedTvsByIdUseCase
import domain.useCase.GetTvByIdUseCase
import domain.useCase.GetTvCreditsByIdUseCase
import domain.useCase.GetTvReviewsByIdUseCase
import domain.useCase.RemoveFavoriteMovieUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class ReviewsPresentableFilter(
    val title: String
)

internal sealed class DetailsScreenViewState() {

    data class MovieDetailSection(
        val movieDetails: MovieDetails,
    ) : DetailsScreenViewState() {
        companion object {
            val initial = MovieDetailSection(
                MovieDetails
                    (
                    id = 0,
                    posterPath = "",
                    genres = emptyList(),
                    originCountry = emptyList(),
                    title = "",
                    overview = "",
                    releaseDate = "",
                    runtime = 0,
                    voteAverage = 0.0,
                    isFavorite = false,
                    movieType = "movie"
                )
            )
        }
    }

    data class MovieCreditsSection(
        val movieCredits: MovieCredits,
    ) : DetailsScreenViewState() {
        companion object {
            val initial = MovieCreditsSection(
                MovieCredits(emptyList(), emptyList(), 0)
            )
        }
    }

    data class MovieReviewsSection(
        val movieReviews: List<MovieReviews>,
        val filters: List<ReviewsPresentableFilter>,
        val selectedTabIndex: Int
    ) : DetailsScreenViewState() {
        companion object {
            val initial = MovieReviewsSection(
                emptyList(), emptyList(), 0
            )
        }
    }

    data class RecommendedMoviesSection(
        val recommendedMovies: List<Movie>,
    ) : DetailsScreenViewState() {
        companion object {
            val initial = RecommendedMoviesSection(
                emptyList()
            )
        }
    }
}

internal abstract class DetailScreenViewModel
    : BaseViewModel<DetailsScreenViewState>() {
    abstract fun selectReviewFilter(reviewFilter: MovieReviewFilter)
    abstract fun insertFavoriteMovies(movieId: Long, posterPath: String, movieType: String)
    abstract fun removeFavoriteMovies(movieId: Long)
    abstract fun formatDateTime(dateTimeString: String): String
    abstract fun calculateUserScore(voteAverage: Double): Double
}

internal class DetailScreenViewModelImpl(
    private val movieId: String,
    private val movieType: String,
    private val getMovieById: GetMovieByIdUseCase,
    private val getMovieCreditsById: GetMovieCreditsByIdUseCase,
    private val getMovieReviewsById: GetMovieReviewsByIdUseCase,
    private val getRecommendedMoviesById: GetRecommendedMoviesByIdUseCase,
    private val getTvById: GetTvByIdUseCase,
    private val getTvCreditsById: GetTvCreditsByIdUseCase,
    private val getTvReviewsById: GetTvReviewsByIdUseCase,
    private val getRecommendedTvsById: GetRecommendedTvsByIdUseCase,
    private val insertFavoriteMovie: AddFavoriteMovieUseCase,
    private val removeFavoriteMovie: RemoveFavoriteMovieUseCase
) : DetailScreenViewModel() {

    private val reviewsFilter = MutableStateFlow(MovieReviewFilter.REVIEWS)

    init {
        query {
            when (movieType) {
                "movie" -> getMovieById(movieId).map(DetailsScreenViewState::MovieDetailSection)
                else -> getTvById(movieId).map(DetailsScreenViewState::MovieDetailSection)
            }
        }

        query {
            when (movieType) {
                "movie" -> getMovieCreditsById(movieId).map(DetailsScreenViewState::MovieCreditsSection)
                else -> getTvCreditsById(movieId).map(DetailsScreenViewState::MovieCreditsSection)
            }
        }

        query {
            when (movieType) {
                "movie" -> reviewsFilter
                    .flatMapLatest { selectedFilter ->
                        combine(
                            getMovieReviewsById(movieId, selectedFilter),
                            flowOf(MovieReviewFilter.entries)
                        ) { reviews, allFilters ->
                            DetailsScreenViewState.MovieReviewsSection(
                                movieReviews = reviews,
                                filters = allFilters.map { filter ->
                                    ReviewsPresentableFilter(
                                        title = filter.toString(),
                                    )
                                },
                                selectedTabIndex = allFilters.indexOf(selectedFilter)
                            )
                        }
                    }

                else -> reviewsFilter
                    .flatMapLatest { selectedFilter ->
                        combine(
                            getTvReviewsById(movieId, selectedFilter),
                            flowOf(MovieReviewFilter.entries)
                        ) { reviews, allFilters ->
                            DetailsScreenViewState.MovieReviewsSection(
                                movieReviews = reviews,
                                filters = allFilters.map { filter ->
                                    ReviewsPresentableFilter(
                                        title = filter.toString(),
                                    )
                                },
                                selectedTabIndex = allFilters.indexOf(selectedFilter)
                            )
                        }
                    }
            }
        }

        query {
            when (movieType) {
                "movie" -> getRecommendedMoviesById(movieId).map(DetailsScreenViewState::RecommendedMoviesSection)
                else -> getRecommendedTvsById(movieId).map(DetailsScreenViewState::RecommendedMoviesSection)
            }

        }
    }


    override fun insertFavoriteMovies(movieId: Long, posterPath: String, movieType: String) {
        runCommand {
            insertFavoriteMovie(movieId, posterPath, movieType)
        }
    }

    override fun removeFavoriteMovies(movieId: Long) {
        runCommand {
            removeFavoriteMovie(movieId)
        }
    }

    override fun selectReviewFilter(reviewFilter: MovieReviewFilter) {
        this.reviewsFilter.update { reviewFilter }
    }

    override fun formatDateTime(dateTimeString: String): String {
        return try {
            val instant = Instant.parse(dateTimeString)
            val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
            localDateTime.date.toString()
        } catch (e: IllegalArgumentException) {
            ""
        }
    }

    override fun calculateUserScore(voteAverage: Double): Double = (voteAverage / 100.0f) * 10

}
