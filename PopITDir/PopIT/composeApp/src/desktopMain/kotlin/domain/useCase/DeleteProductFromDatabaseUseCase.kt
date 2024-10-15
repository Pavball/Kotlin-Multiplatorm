package domain.useCase

import domain.model.Product
import repositories.ProductRepository
import repositories.ServiceRepository


internal interface DeleteProductFromDatabaseUseCase {

    suspend operator fun invoke(productId: Int)

}

internal class DeleteProductFromDatabase(private val productRepository: ProductRepository) :
    DeleteProductFromDatabaseUseCase {
    override suspend fun invoke(productId: Int): Unit =
        productRepository.deleteProductFromDatabase(productId)
}