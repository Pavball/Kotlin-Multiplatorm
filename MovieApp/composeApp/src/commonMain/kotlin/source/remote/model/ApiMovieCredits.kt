package source.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMovieCredits(
    @SerialName("cast")
    val cast: List<ApiCast>,
    @SerialName("crew")
    val crew: List<ApiCrew>,
    @SerialName("id")
    val id: Int
)
