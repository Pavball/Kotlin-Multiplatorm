package domain.useCase

import domain.model.EmployeeDetails
import domain.model.WorkPosition
import kotlinx.coroutines.flow.Flow
import repositories.EmployeeRepository

internal interface GetAllWorkPositionsUseCase {

    operator fun invoke(): Flow<List<WorkPosition>>
}

internal class GetAllWorkPositions(private val employeeRepository: EmployeeRepository) :
    GetAllWorkPositionsUseCase {
    override fun invoke(): Flow<List<WorkPosition>> = employeeRepository.getAllWorkPositions()
}
