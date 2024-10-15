package source.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiGenre(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
