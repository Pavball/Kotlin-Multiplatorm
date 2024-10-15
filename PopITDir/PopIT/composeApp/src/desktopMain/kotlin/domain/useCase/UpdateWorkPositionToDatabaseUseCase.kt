package domain.useCase

import domain.model.WorkPosition
import repositories.EmployeeRepository

internal interface UpdateWorkPositionToDatabaseUseCase {

    suspend operator fun invoke(workPosition: WorkPosition)

}

internal class UpdateWorkPositionToDatabase(private val employeeRepository: EmployeeRepository) :
    UpdateWorkPositionToDatabaseUseCase {
    override suspend fun invoke(workPosition: WorkPosition): Unit =
        employeeRepository.updateWorkPositionToDatabase(workPosition)
}
