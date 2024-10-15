package domain.useCase

import domain.model.EmployeeService
import repositories.EmployeeRepository

internal interface AddEmployeeSeviceToDatabaseUseCase {

    suspend operator fun invoke(employeeService: EmployeeService)

}

internal class AddEmployeeService(private val employeeRepository: EmployeeRepository) :
    AddEmployeeSeviceToDatabaseUseCase {
    override suspend fun invoke(employeeService: EmployeeService): Unit =
        employeeRepository.addEmployeeServiceToDatabase(employeeService)
}
