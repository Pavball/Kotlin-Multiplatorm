package domain.useCase

import domain.model.ServiceRequest
import kotlinx.coroutines.flow.Flow
import repositories.ServiceRepository

internal interface GetAllServiceRequestsUseCase {

    operator fun invoke(): Flow<List<ServiceRequest>>
}

internal class GetAllServiceRequests(private val serviceRepository : ServiceRepository) :
    GetAllServiceRequestsUseCase {
    override fun invoke(): Flow<List<ServiceRequest>> = serviceRepository.getAllServiceRequests()
}
