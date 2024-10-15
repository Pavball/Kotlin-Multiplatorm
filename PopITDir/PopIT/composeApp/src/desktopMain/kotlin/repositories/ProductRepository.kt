package repositories

import domain.model.Product
import domain.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import source.local.LocalDataSource

internal interface ProductRepository{
    fun getAllProducts(): Flow<List<Product>>

    fun getAllProductCategories(): Flow<List<ProductCategory>>

    fun getSpecificProductDetails(productId: Int): Flow<Product>

    fun addProductToDatabase(product: Product)

    fun deleteProductFromDatabase(productId: Int)

    fun updateProductToDatabase(product: Product)

    fun addProductCategoryToDatabase(productCategory: ProductCategory)

    fun deleteProductCategoryFromDatabase(categoryId: Int)

    fun updateProductCategoryToDatabase(productCategory: ProductCategory)


}

internal class ProductRepositoryImpl(
    private val localDataSource: LocalDataSource,
) : ProductRepository {

    override fun getAllProductCategories(): Flow<List<ProductCategory>> {
        return localDataSource.getAllProductCategories()
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return localDataSource.getAllProducts()
    }

    override fun getSpecificProductDetails(productId: Int): Flow<Product> {
        return localDataSource.getSpecificProductDetails(productId)
    }

    override fun addProductToDatabase(product: Product) =
        localDataSource.addProductToDatabase(product)

    override fun deleteProductFromDatabase(productId: Int) {
        localDataSource.deleteProductFromDatabase(productId)
    }

    override fun updateProductToDatabase(product: Product) {
        localDataSource.updateProductToDatabase(product)
    }

    override fun addProductCategoryToDatabase(productCategory: ProductCategory) =
        localDataSource.addProductCategoryToDatabase(productCategory)

    override fun deleteProductCategoryFromDatabase(categoryId: Int) {
        localDataSource.deleteProductCategoryFromDatabase(categoryId)
    }

    override fun updateProductCategoryToDatabase(productCategory: ProductCategory) {
        localDataSource.updateProductCategoryToDatabase(productCategory)
    }

}
