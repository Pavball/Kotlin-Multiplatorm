package ui.viewmodels

import domain.model.EmployeeDetails
import domain.model.Order
import domain.model.ServiceRequest
import domain.model.WorkPosition
import domain.useCase.GetAllServiceRequestsUseCase
import domain.useCase.GetEmployeeDetailsUseCase
import domain.useCase.GetOrdersUseCase
import kotlinx.coroutines.flow.map

internal sealed class HomeScreenViewState {

    data class HomeSection(
        val employeeDetail: EmployeeDetails,
    ) : HomeScreenViewState() {
        companion object {
            val initial = HomeSection(
                EmployeeDetails(
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    WorkPosition(0, "", ""),
                    false,
                    false
                )
            )
        }
    }

    data class OrderNumbersSection(
        val totalOrders: List<Order>,
    ) : HomeScreenViewState() {
        companion object {
            val initial = OrderNumbersSection(
                emptyList()
            )
        }
    }

    data class ServicesNumbersSection(
        val totalServices: List<ServiceRequest>,
    ) : HomeScreenViewState() {
        companion object {
            val initial = ServicesNumbersSection(
                emptyList()
            )
        }
    }
}

internal abstract class HomeScreenViewModel : BaseViewModel<HomeScreenViewState>()

internal class HomeScreenViewModelImpl(
    private val employeeId: Int,
    private val isAdmin: Boolean,
    private val getEmployeeDetailsUseCase: GetEmployeeDetailsUseCase,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getAllServiceRequestsUseCase: GetAllServiceRequestsUseCase
) : HomeScreenViewModel() {

    init {
        query {
            getEmployeeDetailsUseCase(employeeId, isAdmin)
                .map(HomeScreenViewState::HomeSection)
        }

        query {
            getOrdersUseCase()
                .map(HomeScreenViewState::OrderNumbersSection)
        }

        query {
            getAllServiceRequestsUseCase()
                .map(HomeScreenViewState::ServicesNumbersSection)
        }
    }
}
