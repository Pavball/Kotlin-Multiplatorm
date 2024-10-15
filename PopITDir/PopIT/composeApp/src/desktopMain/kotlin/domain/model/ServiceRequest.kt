package domain.model

data class ServiceRequest(
    val requestId: Int,
    val requestDate: String,
    val requestStatus: String,
    val problemDesc: String?,
    val userId: Int?,
    val createdByAdminId: Int?,
    val serviceId: Int,
    val service: Service, // Uključuje informacije o servisu
    val employeeId: Int?,
    val communication: String,
    val userDetails: UserDetails
)
