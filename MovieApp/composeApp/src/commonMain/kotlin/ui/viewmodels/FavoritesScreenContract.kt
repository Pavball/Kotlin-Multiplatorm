package ui.viewmodels

import domain.model.FavoriteMovieDomain
import domain.useCase.GetFavoriteMoviesUseCase
import domain.useCase.RemoveFavoriteMovieUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

internal sealed class FavoriteScreenViewState() {

    data class FavoriteMoviesSection(
        val favoriteMovies: List<FavoriteMovieDomain>,
    ) : FavoriteScreenViewState() {
        companion object {
            val initial = FavoriteMoviesSection(
                emptyList()
            )
        }
    }
}

internal abstract class FavoriteScreenViewModel : BaseViewModel<FavoriteScreenViewState>() {
    abstract fun removeFavoriteMovies(movieId: Long)
}

internal class FavoriteScreenViewModelImpl(
    private val getFavoriteMovies: GetFavoriteMoviesUseCase,
    private val removeFavoriteMovie: RemoveFavoriteMovieUseCase
) : FavoriteScreenViewModel() {

    init {
        fetchFavoriteMovies()
    }

    private fun fetchFavoriteMovies() {
        runCommand {
            getFavoriteMovies().map {
                FavoriteScreenViewState.FavoriteMoviesSection(it)
            }.collectLatest {
                viewState.emit(it)
            }
        }
    }

    override fun removeFavoriteMovies(movieId: Long) {
        runCommand {
            removeFavoriteMovie(movieId)
        }
    }
}
