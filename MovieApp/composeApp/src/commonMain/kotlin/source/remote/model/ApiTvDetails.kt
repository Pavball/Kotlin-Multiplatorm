package source.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiTvDetails(
    @SerialName("genres")
    val genres: List<ApiGenre>,
    @SerialName("id")
    val id: Int,
    @SerialName("origin_country")
    val originCountry: List<String>,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("first_air_date")
    val releaseDate: String,
    @SerialName("episode_run_time")
    val runtime: List<Int>,
    @SerialName("name")
    val title: String,
    @SerialName("vote_average")
    val voteAverage: Double
)
