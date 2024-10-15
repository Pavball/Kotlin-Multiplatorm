package source.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCrew(
    @SerialName("job")
    val job: String,
    @SerialName("name")
    val name: String,
    @SerialName("popularity")
    val popularity: Double,
)
