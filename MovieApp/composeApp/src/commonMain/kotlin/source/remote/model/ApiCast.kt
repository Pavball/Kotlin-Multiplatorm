package source.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCast(
    @SerialName("character")
    val character: String,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?
){
    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/original/$profilePath"
}
