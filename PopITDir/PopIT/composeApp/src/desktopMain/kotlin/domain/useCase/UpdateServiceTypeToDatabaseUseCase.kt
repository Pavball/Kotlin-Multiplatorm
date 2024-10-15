package domain.useCase

import domain.model.Product
import domain.model.Service
import domain.model.ServiceType
import repositories.ProductRepository
import repositories.ServiceRepository


internal interface UpdateServiceTypeToDatabaseUseCase {

    suspend operator fun invoke(serviceType: ServiceType)

}

internal class UpdateServiceTypeToDatabase(private val serviceRepository: ServiceRepository) :
    UpdateServiceTypeToDatabaseUseCase {
    override suspend fun invoke(serviceType: ServiceType): Unit =
        serviceRepository.updateServiceTypeToDatabase(serviceType)
}