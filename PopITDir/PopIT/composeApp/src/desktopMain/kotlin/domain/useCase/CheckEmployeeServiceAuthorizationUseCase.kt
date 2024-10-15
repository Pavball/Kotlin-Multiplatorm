package domain.useCase

import kotlinx.coroutines.flow.Flow
import repositories.ServiceRepository


internal interface CheckEmployeeServiceAuthorizationUseCase {

    suspend operator fun invoke(employeeId: Int, serviceId: Int) : Flow<Boolean>

}

internal class CheckEmployeeServiceAuthorization(private val serviceRepository: ServiceRepository) :
    CheckEmployeeServiceAuthorizationUseCase {
    override suspend fun invoke(employeeId: Int, serviceId: Int): Flow<Boolean> =
        serviceRepository.checkEmployeeServiceAuthorization(employeeId, serviceId)
}