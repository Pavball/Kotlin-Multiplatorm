package ui.viewmodels

import domain.model.EmployeeDetails
import domain.model.Service
import domain.model.ServiceRequest
import domain.useCase.GetAllEmployeesUseCase
import domain.useCase.GetAllServiceRequestsUseCase
import domain.useCase.GetAllServicesUseCase
import kotlinx.coroutines.flow.map

internal sealed class ServicesScreenViewState {

    data class ServiceRequestsSection(
        val serviceRequests: List<ServiceRequest>,
    ) : ServicesScreenViewState() {
        companion object {
            val initial = ServiceRequestsSection(
                emptyList()
            )
        }
    }

    data class ServicesSection(
        val services: List<Service>,
    ) : ServicesScreenViewState() {
        companion object {
            val initial = ServicesSection(
                emptyList()
            )
        }
    }

    data class EmployeeSection(
        val employees: List<EmployeeDetails>,
    ) : ServicesScreenViewState() {
        companion object {
            val initial = EmployeeSection(
                emptyList()
            )
        }
    }

}

internal abstract class ServicesScreenViewModel
    : BaseViewModel<ServicesScreenViewState>()

internal class ServicesScreenViewModelImpl(
    private val getAllServiceRequestsUseCase: GetAllServiceRequestsUseCase,
    private val getAllServicesUseCase: GetAllServicesUseCase,
    private val getAllEmployees: GetAllEmployeesUseCase
) : ServicesScreenViewModel() {

    init {
        query {
            getAllServiceRequestsUseCase().map(ServicesScreenViewState::ServiceRequestsSection)
        }

        query {
            getAllServicesUseCase()
                .map(ServicesScreenViewState::ServicesSection)
        }

        query {
            getAllEmployees()
                .map(ServicesScreenViewState::EmployeeSection)
        }

    }
}
