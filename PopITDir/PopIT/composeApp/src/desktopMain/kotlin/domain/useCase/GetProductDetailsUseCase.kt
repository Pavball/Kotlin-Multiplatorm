package domain.useCase

import domain.model.Product
import kotlinx.coroutines.flow.Flow
import repositories.ProductRepository

internal interface GetProductDetailsUseCase {

    operator fun invoke(productId: Int): Flow<Product>
}

internal class GetProductDetails(private val productRepository: ProductRepository) :
    GetProductDetailsUseCase {
    override fun invoke(productId: Int): Flow<Product> = productRepository.getSpecificProductDetails(productId)
}
