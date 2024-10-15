package ui.viewmodels

import domain.model.Order
import domain.model.UserDetails
import domain.useCase.CompleteOrderUseCase
import domain.useCase.GetOrderDetailsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal sealed class OrderDetailsScreenViewState {

    data class OrderDetailsSection(
        val orderDetail: Order,
    ) : OrderDetailsScreenViewState() {
        companion object {
            val initial = OrderDetailsSection(
                Order(
                    0,
                    0,
                    "",
                    "",
                    0.0,
                    UserDetails("Null", "Null", "Null", "Null", "Null", "Null"),
                    emptyList()
                )
            )
        }
    }
}

internal abstract class OrderDetailsScreenViewModel
    : BaseViewModel<OrderDetailsScreenViewState>(){
        abstract fun completeOrder(orderId: Int)
    }

internal class OrderDetailsScreenViewModelImpl(
    private val orderId: Int,
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase,
    private val completeOrderUseCase: CompleteOrderUseCase

) : OrderDetailsScreenViewModel() {

    init {
        query {
            getOrderDetailsUseCase(orderId).map(OrderDetailsScreenViewState::OrderDetailsSection)
        }
    }

    override fun completeOrder(orderId: Int) {
        runCommand {
            completeOrderUseCase(orderId)
        }

        query {
            getOrderDetailsUseCase(orderId)
                .map(OrderDetailsScreenViewState::OrderDetailsSection)
                .onStart { delay(50) }
        }

    }
}
