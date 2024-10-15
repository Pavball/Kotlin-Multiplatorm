package domain.useCase

import repositories.ProductRepository


internal interface DeleteProductCategoryFromDatabaseUseCase {

    suspend operator fun invoke(categoryId: Int)

}

internal class DeleteProductCategoryFromDatabase(private val productRepository: ProductRepository) :
    DeleteProductCategoryFromDatabaseUseCase {
    override suspend fun invoke(categoryId: Int): Unit =
        productRepository.deleteProductCategoryFromDatabase(categoryId)
}