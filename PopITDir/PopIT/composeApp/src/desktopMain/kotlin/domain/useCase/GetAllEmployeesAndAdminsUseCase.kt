package domain.useCase

import domain.model.EmployeeDetails
import kotlinx.coroutines.flow.Flow
import repositories.EmployeeRepository

internal interface GetAllEmployeesAndAdminsUseCase {

    operator fun invoke(): Flow<List<EmployeeDetails>>
}

internal class GetAllEmployeesAndAdmins(private val employeeRepository: EmployeeRepository) :
    GetAllEmployeesAndAdminsUseCase {
    override fun invoke(): Flow<List<EmployeeDetails>> = employeeRepository.getAllEmployeesAndAdmins()
}
