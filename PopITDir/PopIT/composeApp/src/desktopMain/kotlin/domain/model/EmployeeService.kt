package domain.model

data class EmployeeService (
    val employeeServiceId: Int,
    val serviceId: Int,
    val employeeId: Int,
    val service: Service,
    val employee: EmployeeDetails
)