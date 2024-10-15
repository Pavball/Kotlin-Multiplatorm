package domain.model

data class Order(
    val orderId: Int,
    val userId: Int,
    val orderDate: String,
    val orderStatus: String,
    val totalAmount: Double,
    val userDetails: UserDetails,
    val orderDetails: List<OrderDetail>
)
