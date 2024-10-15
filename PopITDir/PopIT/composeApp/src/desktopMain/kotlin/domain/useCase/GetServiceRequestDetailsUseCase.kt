package domain.useCase

import domain.model.ServiceRequest
import kotlinx.coroutines.flow.Flow
import repositories.ServiceRepository

internal interface GetServiceRequestDetailsUseCase {

    operator fun invoke(requestId: Int): Flow<ServiceRequest>
}

internal class GetServiceRequestDetails(private val serviceRepository: ServiceRepository) :
    GetServiceRequestDetailsUseCase {
    override fun invoke(requestId: Int): Flow<ServiceRequest> =
        serviceRepository.getSpecificServiceRequestDetails(requestId)
}
