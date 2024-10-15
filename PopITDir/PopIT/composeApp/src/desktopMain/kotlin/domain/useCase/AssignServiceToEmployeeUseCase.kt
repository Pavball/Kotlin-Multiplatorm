package domain.useCase

import repositories.ServiceRepository


internal interface AssignServiceToEmployeeUseCase {

    suspend operator fun invoke(requestId: Int, employeeId: Int)

}

internal class AssignServiceToEmployee(private val serviceRepository: ServiceRepository) :
    AssignServiceToEmployeeUseCase {
    override suspend fun invoke(requestId: Int, employeeId: Int): Unit =
        serviceRepository.assignServiceToEmployee(requestId, employeeId)
}