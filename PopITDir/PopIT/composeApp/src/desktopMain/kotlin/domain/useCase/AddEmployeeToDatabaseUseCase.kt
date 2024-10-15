package domain.useCase

import domain.model.EmployeeDetails
import repositories.EmployeeRepository

internal interface AddEmployeeToDatabaseUseCase {

    suspend operator fun invoke(employee: EmployeeDetails)

}

internal class AddEmployeeToDatabase(private val employeeRepository: EmployeeRepository) :
    AddEmployeeToDatabaseUseCase {
    override suspend fun invoke(employee: EmployeeDetails): Unit =
        employeeRepository.addEmployeeToDatabase(employee)
}
