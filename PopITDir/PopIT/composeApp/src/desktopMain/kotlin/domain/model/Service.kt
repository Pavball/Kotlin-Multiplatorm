package domain.model

data class Service(
    val serviceId: Int,
    val serviceName: String,
    val serviceDesc: String?,
    val servicePrice: Double,
    val serviceTypeId: Int?,
    val serviceType: ServiceType,
    val createdByAdminId: Int?
)
