package domain.useCase

import domain.model.Product
import kotlinx.coroutines.flow.Flow
import repositories.ProductRepository

internal interface GetAllProductsUseCase {

    operator fun invoke(): Flow<List<Product>>
}

internal class GetAllProducts(private val productRepository: ProductRepository) :
    GetAllProductsUseCase {
    override fun invoke(): Flow<List<Product>> = productRepository.getAllProducts()
}
