package ui.viewmodels

import domain.model.Service
import domain.model.ServiceRequest
import domain.model.ServiceType
import domain.model.UserDetails
import domain.useCase.AssignServiceToEmployee
import domain.useCase.AssignServiceToEmployeeUseCase
import domain.useCase.CheckEmployeeServiceAuthorizationUseCase
import domain.useCase.FinishServiceRequestUseCase
import domain.useCase.GetServiceRequestDetailsUseCase
import domain.useCase.SendMessageToServiceRequestUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal sealed class ServiceRequestDetailsScreenViewState {

    data class ServiceRequestDetailsSection(
        val serviceDetail: ServiceRequest,
    ) : ServiceRequestDetailsScreenViewState() {
        companion object {

            private val serviceType = ServiceType(0, "", "")
            private val service = Service(0, "", "", 0.0, 0, serviceType, 0)
            private val userDetails = UserDetails("Null", "Null", "Null", "Null", "Null", "Null")

            val initial = ServiceRequestDetailsSection(
                ServiceRequest(
                    requestId = 0,
                    requestDate = "",
                    requestStatus = "",
                    problemDesc = "",
                    userId = 0,
                    createdByAdminId = 0,
                    serviceId = 0,
                    service = service,
                    employeeId = null,
                    communication = "",
                    userDetails = userDetails
                )
            )
        }
    }

    data class IsEmployeeAuthorizedSection(
        val isAuthorized: Boolean,
    ) : ServiceRequestDetailsScreenViewState() {
        companion object {

            val initial = IsEmployeeAuthorizedSection(
                isAuthorized = false
            )
        }
    }
}

internal abstract class ServiceRequestDetailsScreenViewModel
    : BaseViewModel<ServiceRequestDetailsScreenViewState>() {
    abstract fun sendMessageToServiceRequest(requestId: Int, updatedCommunication: String)
    abstract fun assignServiceToEmployee(requestId: Int, employeeId: Int)
    abstract fun finishServiceRequest(requestId: Int, finishedCommunication: String)

}

internal class ServiceRequestDetailsScreenViewModelImpl(
    private val requestId: Int,
    private val employeeId: Int,
    private val serviceId: Int,
    private val getServiceRequestDetailsUseCase: GetServiceRequestDetailsUseCase,
    private val sendMessageToServiceRequestUseCase: SendMessageToServiceRequestUseCase,
    private val checkEmployeeServiceAuthorizationUseCase: CheckEmployeeServiceAuthorizationUseCase,
    private val assignServiceToEmployeeUseCase: AssignServiceToEmployeeUseCase,
    private val finishServiceRequestUseCase: FinishServiceRequestUseCase
) : ServiceRequestDetailsScreenViewModel() {

    init {
        query {
            getServiceRequestDetailsUseCase(requestId)
                .map(ServiceRequestDetailsScreenViewState::ServiceRequestDetailsSection)
                .onEach { println(it.serviceDetail) }
        }

        query {
            checkEmployeeServiceAuthorizationUseCase(employeeId, serviceId)
                .map(ServiceRequestDetailsScreenViewState::IsEmployeeAuthorizedSection)
        }
    }

    override fun sendMessageToServiceRequest(requestId: Int, updatedCommunication: String) {
        runCommand {
            sendMessageToServiceRequestUseCase(requestId, updatedCommunication)
        }

        query {
            getServiceRequestDetailsUseCase(requestId)
                .map(ServiceRequestDetailsScreenViewState::ServiceRequestDetailsSection)
                .onStart { delay(50) }
        }
    }

    override fun assignServiceToEmployee(requestId: Int, employeeId: Int) {
        runCommand {
        assignServiceToEmployeeUseCase(requestId, employeeId)
        }

        query {
            getServiceRequestDetailsUseCase(requestId)
                .map(ServiceRequestDetailsScreenViewState::ServiceRequestDetailsSection)
                .onStart { delay(50) }
        }
    }

    override fun finishServiceRequest(requestId: Int, finishedCommunication: String) {
        runCommand {
            finishServiceRequestUseCase(requestId, finishedCommunication)
        }

        query {
            getServiceRequestDetailsUseCase(requestId)
                .map(ServiceRequestDetailsScreenViewState::ServiceRequestDetailsSection)
                .onStart { delay(50) }
        }
    }


}
