package domain.useCase

import domain.model.Product
import domain.model.Service
import repositories.ProductRepository
import repositories.ServiceRepository


internal interface UpdateServiceToDatabaseUseCase {

    suspend operator fun invoke(service: Service)

}

internal class UpdateServiceToDatabase(private val serviceRepository: ServiceRepository) :
    UpdateServiceToDatabaseUseCase {
    override suspend fun invoke(service: Service): Unit =
        serviceRepository.updateServiceToDatabase(service)
}