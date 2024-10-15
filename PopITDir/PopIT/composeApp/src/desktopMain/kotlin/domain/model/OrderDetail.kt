package domain.model

data class OrderDetail(
    val productName: String,
    val orderQuantity: Int,
    val unitPrice: Double
)
