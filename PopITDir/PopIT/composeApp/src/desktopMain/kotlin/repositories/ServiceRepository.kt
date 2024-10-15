package repositories

import domain.model.Service
import domain.model.ServiceRequest
import domain.model.ServiceType
import kotlinx.coroutines.flow.Flow
import source.local.LocalDataSource

internal interface ServiceRepository{
    fun getAllService(): Flow<List<Service>>

    fun getAllServiceTypes(): Flow<List<ServiceType>>

    fun getAllServiceRequests(): Flow<List<ServiceRequest>>

    fun getSpecificServiceRequestDetails(requestId: Int): Flow<ServiceRequest>

    fun sendMessageToServiceRequest(requestId: Int, updatedCommunication: String)

    fun addServiceToDatabase(service: Service)

    fun deleteServiceFromDatabase(serviceId: Int)

    fun updateServiceToDatabase(service: Service)

    fun addServiceTypeToDatabase(serviceType: ServiceType)

    fun deleteServiceTypeFromDatabase(serviceTypeId: Int)

    fun updateServiceTypeToDatabase(serviceType: ServiceType)

    fun assignServiceToEmployee(requestId: Int, employeeId: Int)

    fun checkEmployeeServiceAuthorization(employeeId: Int, serviceId: Int): Flow<Boolean>

    fun finishServiceRequest(requestId: Int, finishedCommunication: String)



}

internal class ServiceRepositoryImpl(
    private val localDataSource: LocalDataSource,
) : ServiceRepository {
    override fun getAllService(): Flow<List<Service>> {
        return localDataSource.getAllServices()
    }

    override fun getAllServiceTypes(): Flow<List<ServiceType>> {
        return localDataSource.getAllServiceTypes()
    }

    override fun getAllServiceRequests(): Flow<List<ServiceRequest>> {
        return localDataSource.getAllServiceRequests()
    }

    override fun getSpecificServiceRequestDetails(requestId: Int): Flow<ServiceRequest> {
        return localDataSource.getSpecificServiceRequestDetails(requestId)
    }

    override fun sendMessageToServiceRequest(requestId: Int, updatedCommunication: String) =
        localDataSource.sendMessageToServiceRequest(requestId, updatedCommunication)

    override fun addServiceToDatabase(service: Service) {
        localDataSource.addServiceToDatabase(service)
    }

    override fun deleteServiceFromDatabase(serviceId: Int) {
        localDataSource.deleteServiceFromDatabase(serviceId)
    }

    override fun updateServiceToDatabase(service: Service) {
        localDataSource.updateServiceToDatabase(service)
    }

    override fun addServiceTypeToDatabase(serviceType: ServiceType) {
        localDataSource.addServiceTypeToDatabase(serviceType)
    }

    override fun deleteServiceTypeFromDatabase(serviceTypeId: Int) {
        localDataSource.deleteServiceTypeFromDatabase(serviceTypeId)
    }

    override fun updateServiceTypeToDatabase(serviceType: ServiceType) {
        localDataSource.updateServiceTypeToDatabase(serviceType)
    }

    override fun assignServiceToEmployee(requestId: Int, employeeId: Int) {
        localDataSource.assignServiceToEmployee(requestId, employeeId)
    }

    override fun checkEmployeeServiceAuthorization(employeeId: Int, serviceId: Int) : Flow<Boolean> {
        return localDataSource.isEmployeeAuthorizedForService(employeeId, serviceId)
    }

    override fun finishServiceRequest(requestId: Int, finishedCommunication: String) {
        localDataSource.finishServiceRequest(requestId, finishedCommunication)
    }


}
