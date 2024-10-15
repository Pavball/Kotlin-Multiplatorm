package domain.useCase

import domain.model.EmployeeDetails
import domain.model.EmployeeService
import repositories.EmployeeRepository

internal interface UpdateEmployeeServiceToDatabaseUseCase {

    suspend operator fun invoke(employeeService: EmployeeService)

}

internal class UpdateEmployeeService(private val employeeRepository: EmployeeRepository) :
    UpdateEmployeeServiceToDatabaseUseCase {
    override suspend fun invoke(employeeService: EmployeeService): Unit =
        employeeRepository.updateEmployeeServiceToDatabase(employeeService)
}
