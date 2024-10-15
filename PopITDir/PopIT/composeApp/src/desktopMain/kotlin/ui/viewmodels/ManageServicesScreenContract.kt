package ui.viewmodels

import domain.model.Service
import domain.model.ServiceType
import domain.useCase.AddServiceToDatabaseUseCase
import domain.useCase.AddServiceTypeToDatabaseUseCase
import domain.useCase.DeleteServiceFromDatabaseUseCase
import domain.useCase.DeleteServiceTypeFromDatabaseUseCase
import domain.useCase.GetAllServiceTypesUseCase
import domain.useCase.GetAllServicesUseCase
import domain.useCase.UpdateServiceToDatabaseUseCase
import domain.useCase.UpdateServiceTypeToDatabaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal sealed class ManageServicesViewState {

    data class ManageServicesSection(
        val manageServices: List<Service>,
    ) : ManageServicesViewState() {
        companion object {
            val initial = ManageServicesSection(
                emptyList()
            )
        }
    }

    data class ManageServiceTypesSection(
        val manageServiceTypes: List<ServiceType>,
    ) : ManageServicesViewState() {
        companion object {
            val initial = ManageServiceTypesSection(
                emptyList()
            )
        }
    }

}

internal abstract class ManageServicesScreenViewModel
    : BaseViewModel<ManageServicesViewState>(){
    abstract fun addService(service: Service)
    abstract fun deleteService(serviceId: Int)
    abstract fun updateService(service: Service)
    abstract fun addServiceType(serviceType: ServiceType)
    abstract fun deleteServiceType(serviceTypeId: Int)
    abstract fun updateServiceType(serviceType: ServiceType)
    }

internal class ManageServicesScreenViewModelImpl(
    private val getAllServicesUseCase: GetAllServicesUseCase,
    private val getAllServiceTypesUseCase: GetAllServiceTypesUseCase,
    private val addServiceToDatabaseUseCase: AddServiceToDatabaseUseCase,
    private val deleteServiceFromDatabaseUseCase: DeleteServiceFromDatabaseUseCase,
    private val updateServiceToDatabaseUseCase: UpdateServiceToDatabaseUseCase,
    private val addServiceTypeToDatabaseUseCase: AddServiceTypeToDatabaseUseCase,
    private val deleteServiceTypeFromDatabaseUseCase: DeleteServiceTypeFromDatabaseUseCase,
    private val updateServiceTypeToDatabaseUseCase: UpdateServiceTypeToDatabaseUseCase
) : ManageServicesScreenViewModel() {

    init {
        query {
            getAllServicesUseCase()
                .map(ManageServicesViewState::ManageServicesSection)
                .onEach { println(it.manageServices) }
        }

        query {
            getAllServiceTypesUseCase()
                .map(ManageServicesViewState::ManageServiceTypesSection)
                .onEach { println(it.manageServiceTypes) }
        }

    }

    override fun addService(service: Service) {
        runCommand {
            addServiceToDatabaseUseCase(service)
        }

        query {
            getAllServicesUseCase()
                .map(ManageServicesViewState::ManageServicesSection)
                .onStart { delay(50) }
        }
    }

    override fun deleteService(serviceId: Int) {
        runCommand {
            deleteServiceFromDatabaseUseCase(serviceId)
        }

        query {
            getAllServicesUseCase()
                .map(ManageServicesViewState::ManageServicesSection)
                .onStart { delay(50) }
        }
    }

    override fun updateService(service: Service) {
        runCommand {
            updateServiceToDatabaseUseCase(service)
        }

        query {
            getAllServicesUseCase()
                .map(ManageServicesViewState::ManageServicesSection)
                .onStart { delay(50) }
        }
    }

    override fun addServiceType(serviceType: ServiceType) {
        runCommand {
            addServiceTypeToDatabaseUseCase(serviceType)
        }

        query {
            getAllServiceTypesUseCase()
                .map(ManageServicesViewState::ManageServiceTypesSection)
                .onStart { delay(50) }
        }
    }

    override fun deleteServiceType(serviceTypeId: Int) {
        runCommand {
            deleteServiceTypeFromDatabaseUseCase(serviceTypeId)
        }

        query {
            getAllServiceTypesUseCase()
                .map(ManageServicesViewState::ManageServiceTypesSection)
                .onStart { delay(50) }
        }
    }

    override fun updateServiceType(serviceType: ServiceType) {
        runCommand {
            updateServiceTypeToDatabaseUseCase(serviceType)
        }

        query {
            getAllServiceTypesUseCase()
                .map(ManageServicesViewState::ManageServiceTypesSection)
                .onStart { delay(50) }
        }
    }
}
