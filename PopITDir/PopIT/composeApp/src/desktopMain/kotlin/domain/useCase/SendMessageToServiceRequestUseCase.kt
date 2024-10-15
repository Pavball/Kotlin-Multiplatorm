package domain.useCase

import repositories.ServiceRepository


internal interface SendMessageToServiceRequestUseCase {

    suspend operator fun invoke(requestId: Int, updatedCommunication: String)

}

internal class SendMessageToServiceRequest(private val serviceRepository: ServiceRepository) :
    SendMessageToServiceRequestUseCase {
    override suspend fun invoke(requestId: Int, updatedCommunication: String): Unit =
        serviceRepository.sendMessageToServiceRequest(requestId, updatedCommunication)
}