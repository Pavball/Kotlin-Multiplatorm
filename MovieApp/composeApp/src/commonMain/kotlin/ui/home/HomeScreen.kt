package ui.home

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import composetesting.composeapp.generated.resources.Res
import composetesting.composeapp.generated.resources.movieSectionPopularTitle
import composetesting.composeapp.generated.resources.movieSectionTrendingTitle
import composetesting.composeapp.generated.resources.movieSectionWatchNowTitle
import domain.model.Movie
import domain.model.PopularMovieFilter
import domain.model.TrendingMovieFilter
import domain.model.WatchNowFilter
import io.ktor.http.parametersOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import shimmerBrush
import ui.viewmodels.HomeScreenViewModel
import ui.viewmodels.HomeScreenViewState
import ui.viewmodels.PresentableFilter

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()

    val viewPopularState by homeScreenViewModel.viewState<HomeScreenViewState.PopularMoviesSection>()
        .collectAsState(initial = HomeScreenViewState.PopularMoviesSection.initial)

    val viewWatchNowState by homeScreenViewModel.viewState<HomeScreenViewState.WatchNowSection>()
        .collectAsState(initial = HomeScreenViewState.WatchNowSection.initial)

    val viewTrendingState by homeScreenViewModel.viewState<HomeScreenViewState.TrendingMoviesSection>()
        .collectAsState(initial = HomeScreenViewState.TrendingMoviesSection.initial)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium),
    ) {
        Spacer(
            modifier = Modifier
                .padding(top = AppTheme.spacing.medium)
        )

        TabRowSection(
            sectionTitle = stringResource(Res.string.movieSectionPopularTitle),
            sectionTabs = viewPopularState.filters,
            moviesList = viewPopularState.popularMovies,
            selectedTabIndex = viewPopularState.selectedTabIndex,
            onTabSelected = { index ->
                homeScreenViewModel.selectPopularFilter(PopularMovieFilter.entries[index])
            },
            onFavoriteToggle = { movieId, isFavorite ->
                if (!isFavorite) {
                    homeScreenViewModel.removeFavoriteMovies(movieId.toLong())
                } else {
                    val movie = viewPopularState.popularMovies.find { it.id == movieId }
                    if (movie != null) {
                        homeScreenViewModel.insertFavoriteMovies(
                            movieId.toLong(),
                            movie.fullPosterPath,
                            movie.movieType
                        )
                    }
                }
            },
            onCardClick = { movieId, movie ->
                navController.navigate("details/$movieId/${movie.movieType}")
            }
        )

        TabRowSection(
            sectionTitle = stringResource(Res.string.movieSectionWatchNowTitle),
            sectionTabs = viewWatchNowState.filters,
            moviesList = viewWatchNowState.watchNow,
            selectedTabIndex = viewWatchNowState.selectedTabIndex,
            onTabSelected = { index ->
                homeScreenViewModel.selectWatchFilter(WatchNowFilter.entries[index])
            },
            onFavoriteToggle = { movieId, isFavorite ->
                if (!isFavorite) {
                    homeScreenViewModel.removeFavoriteMovies(movieId.toLong())
                } else {
                    val movie = viewWatchNowState.watchNow.find { it.id == movieId }
                    if (movie != null) {
                        homeScreenViewModel.insertFavoriteMovies(
                            movieId.toLong(),
                            movie.fullPosterPath,
                            movie.movieType
                        )
                    }
                }
            },
            onCardClick = { movieId, movie ->
                navController.navigate("details/$movieId/${movie.movieType}")
            }
        )

        TabRowSection(
            sectionTitle = stringResource(Res.string.movieSectionTrendingTitle),
            sectionTabs = viewTrendingState.filters,
            moviesList = viewTrendingState.trendingMovies,
            selectedTabIndex = viewTrendingState.selectedTabIndex,
            onTabSelected = { index ->
                homeScreenViewModel.selectTrendingFilter(TrendingMovieFilter.entries[index])
            },
            onFavoriteToggle = { movieId, isFavorite ->
                if (!isFavorite) {
                    homeScreenViewModel.removeFavoriteMovies(movieId.toLong())
                } else {
                    val movie = viewTrendingState.trendingMovies.find { it.id == movieId }
                    if (movie != null) {
                        homeScreenViewModel.insertFavoriteMovies(
                            movieId.toLong(),
                            movie.fullPosterPath,
                            movie.movieType
                        )
                    }
                }
            },
            onCardClick = { movieId, movie ->
                navController.navigate("details/$movieId/${movie.movieType}")
            }
        )

        Spacer(
            modifier = Modifier
                .padding(top = AppTheme.spacing.extreme)
        )
    }
}

@Composable
private fun TabRowSection(
    sectionTitle: String,
    sectionTabs: List<PresentableFilter>,
    moviesList: List<Movie>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onFavoriteToggle: (Int, Boolean) -> Unit,
    onCardClick: (Int, Movie) -> Unit,
) {

    if (sectionTabs.isNotEmpty() && moviesList.isNotEmpty()) {
        MovieSectionTitle(sectionTitle)
        TabRowComponent(
            tabs = sectionTabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected
        )
        MovieCardList(moviesList, onFavoriteToggle, onCardClick)
    }
}

@Composable
fun TabRowComponent(
    tabs: List<PresentableFilter>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = AppTheme.colors.primary,
        backgroundColor = AppTheme.colors.background,
        edgePadding = AppTheme.spacing.default,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = AppTheme.colors.primary,
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(AppTheme.spacing.extraSmall)
                    .offset(y = ((-10).dp))
            )
        },
        divider = { },
        modifier = Modifier
            .padding(start = AppTheme.spacing.medium)
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                modifier = Modifier.padding(bottom = AppTheme.spacing.medium),
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                }
            ) {
                Text(
                    text = tab.title,
                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
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
internal fun MovieCardList(
    movieCards: List<Movie>,
    onFavoriteToggle: (Int, Boolean) -> Unit,
    onCardClick: (Int, Movie) -> Unit,
) {
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small),
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.smedium),
        content = {
            items(movieCards) { movie ->
                MovieCard(
                    movie = movie,
                    onFavoriteToggle = { isChecked ->
                        onFavoriteToggle(movie.id, isChecked)
                    },
                    onClick = { onCardClick(movie.id, movie) }
                )
            }
        }
    )
}

@Composable
private fun MovieCard(
    movie: Movie,
    onFavoriteToggle: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showShimmer = remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { onClick() }
    ) {
        AsyncImage(
            modifier = Modifier
                .height(200.dp)
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
            onCheckedChange = onFavoriteToggle,
            modifier = modifier
                .padding(start = AppTheme.spacing.smedium, top = AppTheme.spacing.smedium)
                .size(AppTheme.spacing.extraLarge)
        )
    }
}

@Composable
private fun FavoriteButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    var checked by remember { mutableStateOf(isChecked) }

    LaunchedEffect(key1 = isChecked) {
        checked = isChecked
    }

    Box(modifier) {
        IconToggleButton(
            checked = checked,
            onCheckedChange = {
                checked = it
                onCheckedChange(it)
            },
            modifier = Modifier.background(
                AppTheme.colors.primary.copy(alpha = 0.6f),
                shape = CircleShape
            ).size(AppTheme.spacing.extraLarge)
        ) {
            val transition = updateTransition(checked, label = "Checked indicator")

            val tint by transition.animateColor(
                label = "Tint"
            ) { checked ->
                if (checked) Color.White else Color.White
            }

            Icon(
                imageVector = if (checked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .size(AppTheme.spacing.large)
            )
        }
    }
}
