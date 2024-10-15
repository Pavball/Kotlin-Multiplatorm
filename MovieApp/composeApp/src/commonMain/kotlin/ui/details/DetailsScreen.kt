@file:OptIn(KoinExperimentalAPI::class, KoinExperimentalAPI::class)

package ui.details

import AppTheme
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import domain.model.Movie
import domain.model.MovieCredits
import domain.model.MovieDetails
import domain.model.MovieReviewFilter
import domain.model.MovieReviews
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import shimmerBrush
import source.remote.model.ApiCast
import source.remote.model.ApiCrew
import ui.viewmodels.DetailScreenViewModel
import ui.viewmodels.DetailsScreenViewState
import ui.viewmodels.ReviewsPresentableFilter
import kotlin.math.roundToInt

@OptIn(KoinExperimentalAPI::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    movieId: Int,
    movieType: String
) {
    val detailScreenViewModel = koinViewModel<DetailScreenViewModel> {
        parametersOf(movieId.toString(), movieType)
    }

    val viewDetailState by detailScreenViewModel.viewState<DetailsScreenViewState.MovieDetailSection>()
        .collectAsState(initial = DetailsScreenViewState.MovieDetailSection.initial)

    val viewCreditsState by detailScreenViewModel.viewState<DetailsScreenViewState.MovieCreditsSection>()
        .collectAsState(initial = DetailsScreenViewState.MovieCreditsSection.initial)

    val viewReviewsState by detailScreenViewModel.viewState<DetailsScreenViewState.MovieReviewsSection>()
        .collectAsState(initial = DetailsScreenViewState.MovieReviewsSection.initial)

    val viewRecommendedState by detailScreenViewModel.viewState<DetailsScreenViewState.RecommendedMoviesSection>()
        .collectAsState(initial = DetailsScreenViewState.RecommendedMoviesSection.initial)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium),
    ) {

        IntroSection(
            movieDetails = viewDetailState.movieDetails,
            viewModel = detailScreenViewModel,
            onFavoriteToggle = { movieId, isFavorite ->
                if (!isFavorite) {
                    detailScreenViewModel.removeFavoriteMovies(movieId.toLong())
                } else {
                    val movie = viewDetailState.movieDetails
                    detailScreenViewModel.insertFavoriteMovies(
                        movieId.toLong(),
                        movie.fullPosterPath,
                        movie.movieType
                    )
                }
            })

        OverviewSection(
            sectionTitle = "Overview",
            movieDetails = viewDetailState.movieDetails,
            movieCredits = viewCreditsState.movieCredits
        )

        BilledCastSection(
            sectionTitle = "Top Billed Cast",
            movieCredits = viewCreditsState.movieCredits
        )

        SocialSection(
            viewModel = detailScreenViewModel,
            sectionTitle = "Social",
            sectionTabs = viewReviewsState.filters,
            movieReviews = viewReviewsState.movieReviews,
            selectedTabIndex = viewReviewsState.selectedTabIndex,
            onTabSelected = { index ->
                detailScreenViewModel.selectReviewFilter(MovieReviewFilter.entries[index])
            }
        )

        RecommendationSection(
            "Recommendations",
            viewRecommendedState.recommendedMovies,
            onCardClick = { movieId, movie ->
                println("aaa - ${movie.movieType}")
                navController.navigate("details/$movieId/${movie.movieType}")
            })

        Spacer(
            modifier = Modifier
                .padding(top = AppTheme.spacing.extreme)
        )
    }

}

@Composable
fun RecommendationSection(
    sectionTitle: String,
    recommendedMovies: List<Movie>,
    onCardClick: (Int, Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = modifier
                .padding(top = AppTheme.spacing.extraLarge, start = AppTheme.spacing.medium),
            text = sectionTitle,
            style = AppTheme.typography.title
        )

        RecommendedMovieCardList(recommendedMovies, onCardClick)
    }
}

@Composable
internal fun RecommendedMovieCardList(
    recommendedMovieCards: List<Movie>,
    onCardClick: (Int, Movie) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 15.dp),
        content = {
            items(recommendedMovieCards) { movie ->
                RecommendedMovieCard(
                    movie,
                    onClick = {onCardClick(movie.id, movie)
                    }
                )
            }
        }
    )
}

@Composable
private fun RecommendedMovieCard(
    recommendedMovie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showShimmer = remember { mutableStateOf(true) }

    Column(
        modifier = modifier.padding(AppTheme.spacing.small)
            .clickable { onClick() }
    ) {
        AsyncImage(
            modifier = modifier
                .width(180.dp)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(AppTheme.spacing.small))
                .background(shimmerBrush(targetValue = 1300f, showShimmer = showShimmer.value)),
            onSuccess = { showShimmer.value = false },
            model = recommendedMovie.fullPosterPath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Text(
            text = recommendedMovie.title,
            style = AppTheme.typography.biggerSubtitleBold,
            modifier = modifier
                .padding(top = AppTheme.spacing.extraSmall)
                .width(180.dp),
            maxLines = 2
        )
    }
}


@Composable
internal fun IntroSection(
    movieDetails: MovieDetails,
    viewModel: DetailScreenViewModel,
    onFavoriteToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showShimmer = remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .aspectRatio(ratio = 1.2f / 1f)
    ) {
        AsyncImage(
            modifier = modifier
                .background(shimmerBrush(targetValue = 1300f, showShimmer = showShimmer.value)),
            onSuccess = { showShimmer.value = false },
            model = movieDetails.fullPosterPath,
            contentDescription = "${movieDetails.title} movie image",
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 1f)
                        )
                    )
                )
        )

        Column(
            modifier = modifier
                .padding(start = AppTheme.spacing.large, top = AppTheme.spacing.extreme)
                .align(Alignment.CenterStart)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small),
        ) {
            Spacer(
                modifier = Modifier
                    .padding(bottom = AppTheme.spacing.extraLarge)
            )

            CircularProgressWithText(
                modifier = Modifier,
                progress = viewModel.calculateUserScore(movieDetails.voteAverage).toFloat()
            )
            MovieTextDescription(
                modifier = modifier,
                movieDetails = movieDetails
            )

            ButtonsSection(
                modifier = modifier,
                movieDetails,
                onFavoriteToggle = { isChecked ->
                    onFavoriteToggle(movieDetails.id, isChecked)
                }
            )
        }
    }
}

@Composable
internal fun CircularProgressWithText(modifier: Modifier, progress: Float) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small),
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                progress = progress,
                strokeWidth = AppTheme.spacing.extraSmall,
                strokeCap = StrokeCap.Round,
                color = AppTheme.colors.progressBar,
                backgroundColor = AppTheme.colors.progressBarBackground,
            )
            Text(
                text = "${(progress * 100).roundToInt()}%",
                style = AppTheme.typography.body,
                color = AppTheme.colors.background,
            )
        }
        Text(
            text = "User Score",
            style = AppTheme.typography.body,
            color = AppTheme.colors.background,
        )
    }
}

@Composable
internal fun MovieTextDescription(modifier: Modifier, movieDetails: MovieDetails) {
    Column {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
        ) {
            val title = movieDetails.title
            val releaseDate = "(${movieDetails.releaseDate.split("-")[0]})"

            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(title)
                }
                append(" ")

                append(releaseDate)
            }

            Text(
                text = text,
                style = AppTheme.typography.biggerTitle,
                color = AppTheme.colors.background,
            )

        }

        Row(
            modifier = modifier
                .padding(top = AppTheme.spacing.smedium)
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
        ) {
            Text(
                text = movieDetails.releaseDate,
                style = AppTheme.typography.biggerSubtitle,
                color = AppTheme.colors.background,
            )

            Text(
                text = "(${movieDetails.originCountry.joinToString(", ")})",
                style = AppTheme.typography.biggerSubtitle,
                color = AppTheme.colors.background,
            )
        }

        Row(
            modifier = modifier
                .padding(top = AppTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.extraSmall)
        ) {

            val genresText = movieDetails.genres.joinToString(", ") { it.name }
            val runtimeText = "${movieDetails.runtime / 60}h ${movieDetails.runtime % 60}m"

            val text = buildAnnotatedString {
                append(genresText)
                append(" ")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(runtimeText)
                }
            }

            Text(
                text = text,
                style = AppTheme.typography.biggerSubtitle,
                color = AppTheme.colors.background,
            )
        }

    }
}

@Composable
internal fun ButtonsSection(
    modifier: Modifier,
    movieDetails: MovieDetails,
    onFavoriteToggle: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = AppTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.extraSmall)
    ) {
        FavoriteButton(
            isChecked = movieDetails.isFavorite,
            onCheckedChange = onFavoriteToggle,
            modifier = modifier
        )
    }
}

@Composable
private fun FavoriteButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier
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
                AppTheme.colors.favoriteButtonBackground.copy(alpha = 0.6f),
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


@Composable
internal fun OverviewSection(
    sectionTitle: String,
    movieDetails: MovieDetails,
    movieCredits: MovieCredits,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(
                start = AppTheme.spacing.medium,
                top = AppTheme.spacing.smedium,
                end = AppTheme.spacing.medium
            ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small),
    ) {
        Text(
            text = sectionTitle,
            style = AppTheme.typography.title,
            color = AppTheme.colors.primary,
        )

        Text(
            text = movieDetails.overview,
            style = AppTheme.typography.biggerSubtitle,
            color = Color.Black,
        )

        CrewGrid(movieCredits)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CrewGrid(movieCredits: MovieCredits) {
    val topCrew = movieCredits.crew.sortedByDescending { it.popularity }.take(6)

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            topCrew.chunked(3).forEach { crewRow ->
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    crewRow.forEach { crew ->
                        CrewItem(crewMember = crew)
                    }
                }
            }
        }
    }
}

@Composable
fun CrewItem(crewMember: ApiCrew) {
    Column(
        modifier = Modifier
            .width(110.dp)
    ) {
        Text(text = crewMember.name, style = AppTheme.typography.actorNameBold)
        Text(text = crewMember.job, style = AppTheme.typography.biggerSubtitle)
    }
}


@Composable
internal fun BilledCastSection(
    sectionTitle: String,
    movieCredits: MovieCredits,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small),
    ) {

        Row(
            modifier = Modifier.padding(start = AppTheme.spacing.medium)
        ) {
            Text(sectionTitle, style = AppTheme.typography.title)
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.smedium),
            contentPadding = PaddingValues(horizontal = AppTheme.spacing.medium),
            modifier = Modifier.padding(
                start = AppTheme.spacing.default,
                end = AppTheme.spacing.default
            )
        ) {
            items(movieCredits.cast) { castMember ->
                MovieCreditsCard(castMember)
            }
        }
    }
}

@Composable
private fun MovieCreditsCard(
    castMember: ApiCast,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(167.dp)
            .shadow(
                elevation = AppTheme.spacing.small,
                shape = RoundedCornerShape(AppTheme.spacing.small)
            )
            .clip(RoundedCornerShape(AppTheme.spacing.medium)),
    ) {
        Column(
            modifier = modifier
        ) {
            if (!castMember.profilePath.isNullOrEmpty()) {
                ActorImage(castMember.fullPosterPath)
            } else {
                ActorImage("https://static-00.iconduck.com/assets.00/user-icon-1024x1024-dtzturco.png")
            }

            ActorDetails(castMember)
        }
    }
}

@Composable
private fun ActorImage(imageUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = modifier
            .height(180.dp)
            .aspectRatio(ratio = 125f / 136f),
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ActorDetails(castMember: ApiCast, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(AppTheme.spacing.smedium)
            .width(180.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = AppTheme.spacing.extraSmall)
                .width(130.dp),
            text = castMember.name,
            style = AppTheme.typography.actorNameBold,
            color = AppTheme.colors.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
        Text(
            modifier = Modifier
                .width(130.dp),
            text = castMember.character,
            style = AppTheme.typography.subtitle,
            color = AppTheme.colors.secondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Composable
internal fun SocialSection(
    viewModel: DetailScreenViewModel,
    sectionTitle: String,
    sectionTabs: List<ReviewsPresentableFilter>,
    movieReviews: List<MovieReviews>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    if (sectionTabs.isNotEmpty() && movieReviews.isNotEmpty()) {
        Column(
            modifier = modifier
                .padding(
                    start = AppTheme.spacing.medium,
                    top = AppTheme.spacing.smedium
                ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small),
        ) {
            Text(
                text = sectionTitle,
                style = AppTheme.typography.title,
                color = AppTheme.colors.primary,
            )

            TabRowComponent(
                movieReviews = movieReviews,
                tabs = sectionTabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = onTabSelected
            )

            if (movieReviews.isNotEmpty()) {
                ReviewAuthorDetailsSection(viewModel, movieReviews)
            }

        }
    }
}

@Composable
internal fun ReviewAuthorDetailsSection(
    viewModel: DetailScreenViewModel,
    movieReviews: List<MovieReviews>,
    modifier: Modifier = Modifier
) {

    val formattedDate = viewModel.formatDateTime(movieReviews[0].createdAt)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = AppTheme.spacing.small)
            .background(AppTheme.colors.background)
    ) {
        Column(
            modifier = modifier
                .background(AppTheme.colors.background)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = modifier
                        .height(56.dp)
                        .aspectRatio(ratio = 1f / 1f)
                        .clip(CircleShape),
                    model = movieReviews[0].authorDetails.fullAvatarPath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.width(AppTheme.spacing.small))

                Column {
                    Text(
                        text = "A review by ${movieReviews[0].author}",
                        style = AppTheme.typography.biggerSubtitleBold,
                        color = Color.Black
                    )

                    Text(
                        text = "Written by ${movieReviews[0].author} on $formattedDate ",
                        style = AppTheme.typography.caption,
                        color = Color.Gray
                    )
                }
            }

            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.small),
                text = movieReviews[0].content,
                style = AppTheme.typography.subtitle,
            )
        }
    }
}

@Composable
fun TabRowComponent(
    movieReviews: List<MovieReviews>,
    tabs: List<ReviewsPresentableFilter>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableTabRow(
        modifier = modifier
            .padding(top = AppTheme.spacing.smedium),
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
    ) {
        tabs.forEachIndexed { index, tab ->
            Box(
                modifier = Modifier
                    .padding(horizontal = AppTheme.spacing.medium)
            ) {
                Tab(
                    modifier = Modifier.padding(bottom = AppTheme.spacing.medium),
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabSelected(index)
                    }
                ) {
                    Text(
                        text = "${tab.title} (${movieReviews.size})",
                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}
