@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package ui.viewmodels

import domain.useCase.AddFavoriteMovieUseCase
import domain.useCase.GetPopularMoviesUseCase
import domain.useCase.GetTrendingMoviesUseCase
import domain.useCase.GetWatchNowUseCase
import domain.useCase.RemoveFavoriteMovieUseCase
import domain.model.Movie
import domain.model.PopularMovieFilter
import domain.model.TrendingMovieFilter
import domain.model.WatchNowFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

data class PresentableFilter(
    val title: String
)

internal sealed class HomeScreenViewState() {
    data class PopularMoviesSection(
        val popularMovies: List<Movie>,
        val filters: List<PresentableFilter>,
        val selectedTabIndex: Int
    ) : HomeScreenViewState() {
        companion object {
            val initial = PopularMoviesSection(
                emptyList(),
                emptyList(),
                0
            )
        }
    }

    data class TrendingMoviesSection(
        val trendingMovies: List<Movie>,
        val filters: List<PresentableFilter>,
        val selectedTabIndex: Int
    ) : HomeScreenViewState() {
        companion object {
            val initial = TrendingMoviesSection(
                emptyList(),
                emptyList(),
                0
            )
        }
    }

    data class WatchNowSection(
        val watchNow: List<Movie>,
        val filters: List<PresentableFilter>,
        val selectedTabIndex: Int
    ) : HomeScreenViewState() {
        companion object {
            val initial = WatchNowSection(
                emptyList(),
                emptyList(),
                0
            )
        }
    }
}

internal abstract class HomeScreenViewModel : BaseViewModel<HomeScreenViewState>() {
    abstract fun selectWatchFilter(watchNowFilter: WatchNowFilter)
    abstract fun selectTrendingFilter(trendingMovieFilter: TrendingMovieFilter)
    abstract fun selectPopularFilter(popularMovieFilter: PopularMovieFilter)
    abstract fun insertFavoriteMovies(movieId: Long, posterPath: String, movieType: String)
    abstract fun removeFavoriteMovies(movieId: Long)
}

internal class HomeScreenViewModelImpl(
    private val getFavoritePopularMovies: GetPopularMoviesUseCase,
    private val getFavoriteWatchNow: GetWatchNowUseCase,
    private val getFavoriteTrendingMovies: GetTrendingMoviesUseCase,
    private val insertFavoriteMovie: AddFavoriteMovieUseCase,
    private val removeFavoriteMovie: RemoveFavoriteMovieUseCase

) : HomeScreenViewModel() {

    private val watchNowFilter = MutableStateFlow(WatchNowFilter.MOVIES)
    private val trendingMovieFilter = MutableStateFlow(TrendingMovieFilter.DAY)
    private val popularMovieFilter = MutableStateFlow(PopularMovieFilter.MOVIES)

    init {
        fetchPopularMovies()
        fetchWatchNow()
        fetchTrendingMovie()
    }

    private fun fetchPopularMovies() {
        runCommand {
            popularMovieFilter
                .flatMapLatest { selectedFilter ->
                    combine(
                        getFavoritePopularMovies(selectedFilter),
                        flowOf(PopularMovieFilter.entries)
                    ) { movies, allFilters ->
                        HomeScreenViewState.PopularMoviesSection(
                            popularMovies = movies,
                            filters = allFilters.map { filter ->
                                PresentableFilter(
                                    title = filter.toString(),
                                )
                            },
                            selectedTabIndex = allFilters.indexOf(selectedFilter)
                        )
                    }
                }
                .collect {
                    viewState.emit(it)
                }
        }
    }

    private fun fetchWatchNow() {
        runCommand {
            watchNowFilter
                .flatMapLatest { selectedFilter ->
                    combine(
                        getFavoriteWatchNow(selectedFilter),
                        flowOf(WatchNowFilter.entries)
                    ) { movies, allFilters ->
                        HomeScreenViewState.WatchNowSection(
                            watchNow = movies,
                            filters = allFilters.map { filter ->
                                PresentableFilter(
                                    title = filter.toString(),
                                )
                            },
                            selectedTabIndex = allFilters.indexOf(selectedFilter)
                        )
                    }
                }
                .collect {
                    viewState.emit(it)
                }
        }
    }

    private fun fetchTrendingMovie() {
        runCommand {
            trendingMovieFilter
                .flatMapLatest { selectedFilter ->
                    combine(
                        getFavoriteTrendingMovies(selectedFilter),
                        flowOf(TrendingMovieFilter.entries),
                    ) { movies, allFilters ->
                        HomeScreenViewState.TrendingMoviesSection(
                            trendingMovies = movies,
                            filters = allFilters.map { filter ->
                                PresentableFilter(
                                    title = filter.toString(),
                                )
                            },
                            selectedTabIndex = allFilters.indexOf(selectedFilter)
                        )
                    }
                }
                .collect {
                    viewState.emit(it)
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

    override fun selectWatchFilter(watchNowFilter: WatchNowFilter) {
        this.watchNowFilter.update { watchNowFilter }
    }

    override fun selectTrendingFilter(trendingMovieFilter: TrendingMovieFilter) {
        this.trendingMovieFilter.update { trendingMovieFilter }
    }

    override fun selectPopularFilter(popularMovieFilter: PopularMovieFilter) {
        this.popularMovieFilter.update { popularMovieFilter }
    }
}

