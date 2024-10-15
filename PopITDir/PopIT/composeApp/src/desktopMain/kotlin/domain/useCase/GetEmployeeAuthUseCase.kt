package domain.useCase

import domain.model.EmployeeDetails
import kotlinx.coroutines.flow.Flow
import repositories.EmployeeRepository

internal interface GetEmployeeAuthUseCase {
    operator fun invoke(email: String, password: String, isAdmin: Boolean): Flow<EmployeeDetails>
}

internal class GetEmployeeAuth(private val employeeRepository: EmployeeRepository) :
    GetEmployeeAuthUseCase {
    override fun invoke(email: String, password: String, isAdmin: Boolean): Flow<EmployeeDetails> {
        return if (isAdmin) {
            employeeRepository.getAdminAuth(email, password, isAdmin)
        } else {
            employeeRepository.getEmployeeAuth(email, password, isAdmin)
        }
    }
}
