package ui.viewmodels

import domain.model.Order
import domain.useCase.GetOrdersUseCase
import kotlinx.coroutines.flow.map

internal sealed class OrdersScreenViewState {

    data class OrdersSection(
        val orders: List<Order>,
    ) : OrdersScreenViewState() {
        companion object {
            val initial = OrdersSection(
                emptyList()
            )
        }
    }

}

internal abstract class OrdersScreenViewModel
    : BaseViewModel<OrdersScreenViewState>()

internal class OrdersScreenViewModelImpl(
    private val getOrdersUseCase: GetOrdersUseCase
) : OrdersScreenViewModel() {

    init {
        query {
            getOrdersUseCase().map(OrdersScreenViewState::OrdersSection)
        }
    }

}
