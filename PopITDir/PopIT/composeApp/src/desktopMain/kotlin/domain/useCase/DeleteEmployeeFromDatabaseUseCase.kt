package domain.useCase

import domain.model.EmployeeDetails
import repositories.EmployeeRepository

internal interface DeleteEmployeeFromDatabaseUseCase {

    suspend operator fun invoke(employee: EmployeeDetails)

}

internal class DeleteEmployeeFromDatabase(private val employeeRepository: EmployeeRepository) :
    DeleteEmployeeFromDatabaseUseCase {
    override suspend fun invoke(employee: EmployeeDetails): Unit =
        employeeRepository.deleteEmployeeFromDatabase(employee)
}
