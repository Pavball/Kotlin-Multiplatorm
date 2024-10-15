package domain.useCase

import domain.model.Product
import domain.model.ProductCategory
import repositories.ProductRepository


internal interface UpdateProductCategoryToDatabaseUseCase {

    suspend operator fun invoke(productCategory: ProductCategory)

}

internal class UpdateProductCategoryToDatabase(private val productRepository: ProductRepository) :
    UpdateProductCategoryToDatabaseUseCase {
    override suspend fun invoke(productCategory: ProductCategory): Unit =
        productRepository.updateProductCategoryToDatabase(productCategory)
}