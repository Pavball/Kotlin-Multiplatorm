package domain.model

import source.remote.model.ApiMovieReviews

data class MovieReviews(
    val author: String,
    val authorDetails: ReviewAuthorDetails,
    val content: String,
    val createdAt: String,
    val id: String,
    val updatedAt: String,
    val url: String
)

internal fun ApiMovieReviews.toMovieReviews() =
    MovieReviews(
        author = author,
        authorDetails = authorDetails.toAuthorDetails(),
        content = content,
        createdAt = createdAt,
        id = id,
        updatedAt = updatedAt,
        url = url,
    )
