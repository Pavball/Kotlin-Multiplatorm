package domain.useCase

import domain.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import repositories.ProductRepository

internal interface GetAllProductCategoryUseCase {

    operator fun invoke(): Flow<List<ProductCategory>>
}

internal class GetProductCategory(private val productRepository: ProductRepository) :
    GetAllProductCategoryUseCase {
    override fun invoke(): Flow<List<ProductCategory>> = productRepository.getAllProductCategories()
}
