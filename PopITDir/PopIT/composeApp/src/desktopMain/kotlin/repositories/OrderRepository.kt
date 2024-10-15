package repositories

import domain.model.Order
import kotlinx.coroutines.flow.Flow
import source.local.LocalDataSource
import source.local.LocalDataSourceImpl

internal interface OrderRepository{
    fun getAllOrders(): Flow<List<Order>>

    fun getSpecificOrderDetails(orderId: Int): Flow<Order>

    fun completeOrder(orderId: Int)
}

internal class OrderRepositoryImpl(
    private val localDataSource: LocalDataSource
) : OrderRepository {


    override fun getAllOrders(): Flow<List<Order>> {
        return localDataSource.getAllOrders()
    }

    override fun getSpecificOrderDetails(orderId: Int): Flow<Order> {
        return localDataSource.getSpecificOrderDetails(orderId)
    }

    override fun completeOrder(orderId: Int) {
        localDataSource.completeOrder(orderId)
    }

}
