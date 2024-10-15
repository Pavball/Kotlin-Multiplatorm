package domain.useCase

import domain.model.Service
import kotlinx.coroutines.flow.Flow
import repositories.ServiceRepository

internal interface GetAllServicesUseCase {

    operator fun invoke(): Flow<List<Service>>
}

internal class GetAllServices(private val serviceRepository: ServiceRepository) :
    GetAllServicesUseCase {
    override fun invoke(): Flow<List<Service>> = serviceRepository.getAllService()
}
