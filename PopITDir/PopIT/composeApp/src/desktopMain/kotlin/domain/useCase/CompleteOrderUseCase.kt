package domain.useCase

import repositories.OrderRepository
import repositories.ServiceRepository


internal interface CompleteOrderUseCase {

    suspend operator fun invoke(orderId: Int)

}

internal class CompleteOrder(private val orderRepository: OrderRepository) :
    CompleteOrderUseCase {
    override suspend fun invoke(orderId: Int): Unit =
        orderRepository.completeOrder(orderId)
}