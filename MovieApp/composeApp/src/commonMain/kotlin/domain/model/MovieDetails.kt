package domain.model

import source.remote.model.ApiMovieDetails
import source.remote.model.ApiTvDetails
import source.remote.model.ApiGenre
import kotlin.math.roundToInt

data class MovieDetails(
    val id: Int,
    val posterPath: String,
    val genres: List<ApiGenre>,
    val originCountry: List<String>,
    val title: String,
    val overview : String,
    val releaseDate: String,
    val runtime: Int,
    val voteAverage: Double,
    val isFavorite: Boolean,
    val movieType: String
) {
    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/original/$posterPath"
}

internal fun ApiMovieDetails.toMovieDetails() =
    MovieDetails(
        id = id,
        posterPath = posterPath,
        genres = genres,
        originCountry = originCountry,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        runtime = runtime,
        voteAverage = voteAverage,
        isFavorite = false,
        movieType = "movie"
    )

internal fun ApiTvDetails.toMovieDetails() =
    MovieDetails(
        id = id,
        posterPath = posterPath,
        genres = genres,
        originCountry = originCountry,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        runtime = if (runtime.isNotEmpty()) runtime.average().roundToInt() else 0,
        voteAverage = voteAverage,
        isFavorite = false,
        movieType = "tv"
    )
