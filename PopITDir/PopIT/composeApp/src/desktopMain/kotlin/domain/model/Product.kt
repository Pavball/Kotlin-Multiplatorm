package domain.model

data class Product(
    val productId: Int,
    val productName: String,
    val productDesc: String,
    val productPrice: Double,
    val productStock: Int,
    val productImage: String,
    val categoryId: Int,
    val productCategory: ProductCategory,
    val createdByAdminId: Int?
)
