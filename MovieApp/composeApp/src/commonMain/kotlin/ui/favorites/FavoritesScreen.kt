@file:OptIn(KoinExperimentalAPI::class)

package ui.favorites

import AppTheme
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import domain.model.FavoriteMovieDomain
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import shimmerBrush
import ui.viewmodels.FavoriteScreenViewModel
import ui.viewmodels.FavoriteScreenViewState

@Composable
fun FavoritesScreen(navController: NavController) {
    val favoriteScreenViewModel = koinViewModel<FavoriteScreenViewModel>()

    val viewFavoriteState by favoriteScreenViewModel.viewState<FavoriteScreenViewState.FavoriteMoviesSection>()
        .collectAsState(initial = FavoriteScreenViewState.FavoriteMoviesSection.initial)

    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
    ) {
        Spacer(
            modifier = Modifier
                .padding(top = AppTheme.spacing.medium)
        )

        GridSection(
            "Favorites",
            viewFavoriteState.favoriteMovies,
            onFavoriteToggle = { favoriteMovie, isFavorite ->
                if (!isFavorite) {
                    favoriteScreenViewModel.removeFavoriteMovies(favoriteMovie.movieId.toLong())
                }
            },
            onCardClick = { movieId, movie ->
                println("aaa - ${movie.movieType}")
                navController.navigate("details/$movieId/${movie.movieType}")
            })

        Spacer(
            modifier = Modifier
                .padding(bottom = AppTheme.spacing.extreme)
        )
    }
}

@Composable
internal fun GridSection(
    sectionTitle: String,
    favoriteMoviesList: List<FavoriteMovieDomain>,
    onFavoriteToggle: (FavoriteMovieDomain, Boolean) -> Unit,
    onCardClick: (Int, FavoriteMovieDomain) -> Unit
) {
    MovieSectionTitle(sectionTitle)

    if (favoriteMoviesList.isNotEmpty()) {
        FavoriteMovieCardGrid(favoriteMoviesList, onFavoriteToggle, onCardClick)
    } else {
        IfNoFavorite()
    }
}

@Composable
fun MovieSectionTitle(movieSectionTitle: String) {
    Row(
        modifier = Modifier
            .padding(start = AppTheme.spacing.medium)
    ) {
        Text(
            text = movieSectionTitle,
            style = AppTheme.typography.title,
            color = AppTheme.colors.primary
        )
    }
}

@Composable
internal fun FavoriteMovieCardGrid(
    movieCards: List<FavoriteMovieDomain>,
    onFavoriteToggle: (FavoriteMovieDomain, Boolean) -> Unit,
    onCardClick: (Int, FavoriteMovieDomain) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.padding(bottom = AppTheme.spacing.extreme),
        contentPadding = PaddingValues(AppTheme.spacing.smedium),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.extraLarge),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small),
        content = {
            items(movieCards) { movie ->
                MovieCard(
                    movie = movie,
                    onFavoriteToggle = { isChecked ->
                        onFavoriteToggle(movie, isChecked)
                    },
                    onClick = { onCardClick(movie.movieId, movie) }
                )
            }
        }
    )
}

@Composable
internal fun IfNoFavorite() {
    Text(
        "You're Favorites list is currently empty.",
        modifier = Modifier.padding(start = AppTheme.spacing.smedium)
    )
}

@Composable
private fun MovieCard(
    movie: FavoriteMovieDomain,
    onFavoriteToggle: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    val showShimmer = remember { mutableStateOf(true) }

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable { onClick() }
    ) {
        AsyncImage(
            modifier = Modifier
                .aspectRatio(ratio = 2f / 3f)
                .clip(RoundedCornerShape(AppTheme.spacing.medium))
                .background(shimmerBrush(targetValue = 1300f, showShimmer = showShimmer.value)),
            onSuccess = { showShimmer.value = false },
            model = movie.fullPosterPath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        FavoriteButton(
            isChecked = movie.isFavorite,
            onCheckedChange = { isChecked ->
                onFavoriteToggle(isChecked)
            },
            modifier = Modifier
                .padding(start = AppTheme.spacing.smedium, top = AppTheme.spacing.smedium)
                .size(AppTheme.spacing.extraLarge)
        )
    }
}

@Composable
private fun FavoriteButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    Box(modifier) {
        IconToggleButton(
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            },
            modifier = Modifier.background(
                AppTheme.colors.primary.copy(alpha = 0.6f),
                shape = CircleShape
            ).size(AppTheme.spacing.extraLarge)
        ) {
            val transition = updateTransition(isChecked, label = "Checked indicator")

            val tint by transition.animateColor(
                label = "Tint"
            ) { checked ->
                if (checked) Color.White else Color.White
            }

            Icon(
                imageVector = if (isChecked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .size(AppTheme.spacing.large)
            )
        }
    }
}
