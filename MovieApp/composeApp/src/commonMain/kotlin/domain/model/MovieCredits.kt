package domain.model

import source.remote.model.ApiMovieCredits
import source.remote.model.ApiCast
import source.remote.model.ApiCrew

data class MovieCredits(
    val cast: List<ApiCast>,
    val crew: List<ApiCrew>,
    val id: Int
)

internal fun ApiMovieCredits.toMovieDetails() =
    MovieCredits(
        id = id,
        cast = cast,
        crew = crew
    )
