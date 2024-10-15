package domain.useCase

import repositories.EmployeeRepository

internal interface DeleteWorkPositionFromDatabaseUseCase {

    suspend operator fun invoke(positionId: Int)

}

internal class DeleteWorkPositionFromDatabase(private val employeeRepository: EmployeeRepository) :
    DeleteWorkPositionFromDatabaseUseCase {
    override suspend fun invoke(positionId: Int): Unit =
        employeeRepository.deleteWorkPositionFromDatabase(positionId)
}
