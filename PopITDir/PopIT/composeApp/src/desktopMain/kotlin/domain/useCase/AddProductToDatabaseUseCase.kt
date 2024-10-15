package domain.useCase

import domain.model.Product
import repositories.ProductRepository


internal interface AddProductToDatabaseUseCase {

    suspend operator fun invoke(product: Product)

}

internal class AddProductToDatabase(private val productRepository: ProductRepository) :
    AddProductToDatabaseUseCase {
    override suspend fun invoke(product: Product): Unit =
        productRepository.addProductToDatabase(product)
}