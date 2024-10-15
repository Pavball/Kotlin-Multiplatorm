package source.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiMovie(
    @SerialName("id")
    val id: Int,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("title")
    val title: String,
)
