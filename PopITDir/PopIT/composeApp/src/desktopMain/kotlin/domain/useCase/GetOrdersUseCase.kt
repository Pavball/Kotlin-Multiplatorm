package domain.useCase

import domain.model.Order
import kotlinx.coroutines.flow.Flow
import repositories.OrderRepository

internal interface GetOrdersUseCase {
    operator fun invoke(): Flow<List<Order>>
}

internal class GetOrders(private val orderRepository: OrderRepository) :
    GetOrdersUseCase {
    override fun invoke(): Flow<List<Order>> = orderRepository.getAllOrders()
}

