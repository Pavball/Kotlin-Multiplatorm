package domain.useCase

import domain.model.Product
import domain.model.Service
import repositories.ServiceRepository


internal interface AddServiceToDatabaseUseCase {

    suspend operator fun invoke(service: Service)

}

internal class AddServiceToDatabase(private val serviceRepository: ServiceRepository) :
    AddServiceToDatabaseUseCase {
    override suspend fun invoke(service: Service): Unit =
        serviceRepository.addServiceToDatabase(service)
}