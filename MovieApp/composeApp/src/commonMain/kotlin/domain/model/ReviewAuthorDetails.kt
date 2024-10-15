package domain.model

import source.remote.model.ApiAuthorDetails

data class ReviewAuthorDetails(
    val avatarPath: String?,
    val name: String,
    val rating: Double?,
    val username: String
){
    val fullAvatarPath: String
        get() = "https://image.tmdb.org/t/p/original/$avatarPath"
}

internal fun ApiAuthorDetails.toAuthorDetails() =
    ReviewAuthorDetails(
        avatarPath = avatarPath,
        name = name,
        rating = rating,
        username = username,
    )
