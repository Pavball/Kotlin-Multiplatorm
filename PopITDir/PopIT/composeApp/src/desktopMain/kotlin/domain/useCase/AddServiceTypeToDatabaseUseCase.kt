package domain.useCase

import domain.model.Product
import domain.model.Service
import domain.model.ServiceType
import repositories.ServiceRepository


internal interface AddServiceTypeToDatabaseUseCase {

    suspend operator fun invoke(serviceType: ServiceType)

}

internal class AddServiceTypeToDatabase(private val serviceRepository: ServiceRepository) :
    AddServiceTypeToDatabaseUseCase {
    override suspend fun invoke(serviceType: ServiceType): Unit =
        serviceRepository.addServiceTypeToDatabase(serviceType)
}