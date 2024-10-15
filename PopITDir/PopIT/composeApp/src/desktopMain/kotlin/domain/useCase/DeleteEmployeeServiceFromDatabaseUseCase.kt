package domain.useCase

import domain.model.EmployeeDetails
import domain.model.EmployeeService
import domain.model.Service
import repositories.EmployeeRepository

internal interface DeleteEmployeeServiceFromDatabaseUseCase {

    suspend operator fun invoke(employeeService: EmployeeService)

}

internal class DeleteEmployeeService(private val employeeRepository: EmployeeRepository) :
    DeleteEmployeeServiceFromDatabaseUseCase {
    override suspend fun invoke(employeeService: EmployeeService): Unit =
        employeeRepository.deleteEmployeeServiceFromDatabase(employeeService)
}
