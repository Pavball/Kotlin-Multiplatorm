package domain.useCase

import repositories.OrderRepository
import repositories.ServiceRepository


internal interface FinishServiceRequestUseCase {

    suspend operator fun invoke(requestId: Int, finishedCommunication: String)

}

internal class FinishServiceRequest(private val serviceRepository: ServiceRepository) :
    FinishServiceRequestUseCase {
    override suspend fun invoke(requestId: Int, finishedCommunication: String): Unit =
        serviceRepository.finishServiceRequest(requestId, finishedCommunication)
}