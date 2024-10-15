package domain.useCase

import domain.model.Product
import repositories.ProductRepository
import repositories.ServiceRepository


internal interface DeleteServiceFromDatabaseUseCase {

    suspend operator fun invoke(serviceId: Int)

}

internal class DeleteServiceFromDatabase(private val serviceRepository: ServiceRepository) :
    DeleteServiceFromDatabaseUseCase {
    override suspend fun invoke(serviceId: Int): Unit =
        serviceRepository.deleteServiceFromDatabase(serviceId)
}