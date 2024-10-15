package domain.useCase

import domain.model.Service
import domain.model.ServiceType
import kotlinx.coroutines.flow.Flow
import repositories.ServiceRepository

internal interface GetAllServiceTypesUseCase {

    operator fun invoke(): Flow<List<ServiceType>>
}

internal class GetAllServiceTypes(private val serviceRepository: ServiceRepository) :
    GetAllServiceTypesUseCase {
    override fun invoke(): Flow<List<ServiceType>> = serviceRepository.getAllServiceTypes()
}
