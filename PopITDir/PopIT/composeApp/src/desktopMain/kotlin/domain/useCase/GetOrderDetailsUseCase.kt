package domain.useCase

import domain.model.Order
import kotlinx.coroutines.flow.Flow
import repositories.OrderRepository

internal interface GetOrderDetailsUseCase {

    operator fun invoke(orderId: Int): Flow<Order>
}

internal class GetOrderDetails(private val orderRepository: OrderRepository) :
    GetOrderDetailsUseCase {
    override fun invoke(orderId: Int): Flow<Order> = orderRepository.getSpecificOrderDetails(orderId)
}
