package domain.useCase

import domain.model.EmployeeDetails
import repositories.EmployeeRepository

internal interface UpdateEmployeeToDatabaseUseCase {

    suspend operator fun invoke(employee: EmployeeDetails)

}

internal class UpdateEmployeeToDatabase(private val employeeRepository: EmployeeRepository) :
    UpdateEmployeeToDatabaseUseCase {
    override suspend fun invoke(employee: EmployeeDetails): Unit =
        employeeRepository.updateEmployeeToDatabase(employee)
}
