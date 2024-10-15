package domain.useCase

import domain.model.Product
import repositories.ProductRepository
import repositories.ServiceRepository


internal interface DeleteServiceTypeFromDatabaseUseCase {

    suspend operator fun invoke(serviceTypeId: Int)

}

internal class DeleteServiceTypeFromDatabase(private val serviceRepository: ServiceRepository) :
    DeleteServiceTypeFromDatabaseUseCase {
    override suspend fun invoke(serviceTypeId: Int): Unit =
        serviceRepository.deleteServiceTypeFromDatabase(serviceTypeId)
}