package source.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMovieDetails(
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
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("runtime")
    val runtime: Int,
    @SerialName("title")
    val title: String,
    @SerialName("vote_average")
    val voteAverage: Double
)
