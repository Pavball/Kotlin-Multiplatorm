package domain.useCase

import domain.model.EmployeeDetails
import kotlinx.coroutines.flow.Flow
import repositories.EmployeeRepository

internal interface GetAllEmployeesUseCase {

    operator fun invoke(): Flow<List<EmployeeDetails>>
}

internal class GetAllEmployees(private val employeeRepository: EmployeeRepository) :
    GetAllEmployeesUseCase {
    override fun invoke(): Flow<List<EmployeeDetails>> = employeeRepository.getAllEmployees()
}
