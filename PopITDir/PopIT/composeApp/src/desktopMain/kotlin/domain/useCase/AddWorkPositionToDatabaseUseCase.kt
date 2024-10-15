package domain.useCase

import domain.model.EmployeeDetails
import domain.model.WorkPosition
import repositories.EmployeeRepository

internal interface AddWorkPositionToDatabaseUseCase {

    suspend operator fun invoke(workPosition: WorkPosition)

}

internal class AddWorkPositionToDatabase(private val employeeRepository: EmployeeRepository) :
    AddWorkPositionToDatabaseUseCase {
    override suspend fun invoke(workPosition: WorkPosition): Unit =
        employeeRepository.addWorkPositionToDatabase(workPosition)
}
