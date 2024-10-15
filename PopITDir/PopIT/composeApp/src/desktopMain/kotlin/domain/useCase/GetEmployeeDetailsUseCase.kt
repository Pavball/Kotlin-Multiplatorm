package domain.useCase

import domain.model.EmployeeDetails
import kotlinx.coroutines.flow.Flow
import repositories.EmployeeRepository

internal interface GetEmployeeDetailsUseCase {
    operator fun invoke(employeeId: Int, isAdmin: Boolean): Flow<EmployeeDetails>
}

internal class GetEmployeeDetails(private val employeeRepository: EmployeeRepository) :
    GetEmployeeDetailsUseCase {
    override fun invoke(employeeId: Int, isAdmin: Boolean): Flow<EmployeeDetails> {
        return employeeRepository.getEmployeeDetails(employeeId, isAdmin)
    }
}
