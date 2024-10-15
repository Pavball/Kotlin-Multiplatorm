package domain.model

data class EmployeeDetails(
    val employeeId: Int,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val workPositionId: Int?,
    val workPosition: WorkPosition?,
    val isAdmin: Boolean,
    val roleChanged: Boolean
)
