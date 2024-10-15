package domain.useCase

import domain.model.Product
import repositories.ProductRepository


internal interface UpdateProductToDatabaseUseCase {

    suspend operator fun invoke(product: Product)

}

internal class UpdateProductToDatabase(private val productRepository: ProductRepository) :
    UpdateProductToDatabaseUseCase {
    override suspend fun invoke(product: Product): Unit =
        productRepository.updateProductToDatabase(product)
}