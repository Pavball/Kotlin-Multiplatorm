package domain.model

import source.remote.model.ApiMovie
import source.remote.model.ApiTv

data class Movie(
    val id: Int,
    val posterPath: String,
    val title: String,
    val isFavorite: Boolean,
    val movieType: String
) {
    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/original/$posterPath"
}

internal fun ApiMovie.toMovie() =
    Movie(
        id = id,
        posterPath = posterPath,
        title = title,
        isFavorite = false,
        movieType = "movie"
    )

internal fun ApiTv.toMovie() =
    Movie(
        id = id,
        posterPath = posterPath,
        title = name,
        isFavorite = false,
        movieType = "tv"
    )
