package domain.useCase

import domain.model.EmployeeService
import kotlinx.coroutines.flow.Flow
import repositories.EmployeeRepository

internal interface GetAllEmployeeServicesUseCase {

    operator fun invoke(): Flow<List<EmployeeService>>
}

internal class GetAllEmployeeServices(private val employeeRepository: EmployeeRepository) :
    GetAllEmployeeServicesUseCase {
    override fun invoke(): Flow<List<EmployeeService>> = employeeRepository.getAllEmployeeServices()
}
