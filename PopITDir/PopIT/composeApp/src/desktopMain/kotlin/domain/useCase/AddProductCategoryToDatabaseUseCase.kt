package domain.useCase

import domain.model.Product
import domain.model.ProductCategory
import repositories.ProductRepository


internal interface AddProductCategoryToDatabaseUseCase {

    suspend operator fun invoke(productCategory: ProductCategory)

}

internal class AddProductCategoryToDatabase(private val productRepository: ProductRepository) :
    AddProductCategoryToDatabaseUseCase {
    override suspend fun invoke(productCategory: ProductCategory): Unit =
        productRepository.addProductCategoryToDatabase(productCategory)
}