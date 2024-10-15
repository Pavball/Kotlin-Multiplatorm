package domain.model

data class UserDetails(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val streetNo: String,
    val city: String,
    val postalCode: String
)
