package domain.model

import org.example.project.FavoriteMovie

data class FavoriteMovieDomain(
    val movieId: Int,
    val posterPath: String,
    val isFavorite: Boolean,
    val movieType: String
) {
    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/original/$posterPath"
}

internal fun FavoriteMovie.toFavoriteMovie() =
    FavoriteMovieDomain(
        movieId = movieId.toInt(),
        posterPath = fullPosterPath,
        isFavorite = true,
        movieType = movieType
    )
