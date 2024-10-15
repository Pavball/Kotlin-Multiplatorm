package ui.viewmodels

import domain.model.Product
import domain.model.ProductCategory
import domain.useCase.AddProductCategoryToDatabaseUseCase
import domain.useCase.AddProductToDatabaseUseCase
import domain.useCase.DeleteProductCategoryFromDatabaseUseCase
import domain.useCase.DeleteProductFromDatabaseUseCase
import domain.useCase.GetAllProductCategoryUseCase
import domain.useCase.GetAllProductsUseCase
import domain.useCase.UpdateProductCategoryToDatabaseUseCase
import domain.useCase.UpdateProductToDatabaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal sealed class ManageProductsViewState {

    data class ManageProductsSection(
        val manageProducts: List<Product>,
    ) : ManageProductsViewState() {
        companion object {
            val initial = ManageProductsSection(
                emptyList()
            )
        }
    }

    data class ManageProductCategorySection(
        val manageProductCategories: List<ProductCategory>,
    ) : ManageProductsViewState() {
        companion object {
            val initial = ManageProductCategorySection(
                emptyList()
            )
        }
    }

}

internal abstract class ManageProductsScreenViewModel
    : BaseViewModel<ManageProductsViewState>(){
    abstract fun addProduct(product: Product)
    abstract fun deleteProduct(productId: Int)
    abstract fun updateProduct(product: Product)
    abstract fun addProductCategory(productCategory: ProductCategory)
    abstract fun deleteProductCategory(categoryId: Int)
    abstract fun updateProductCategory(productCategory: ProductCategory)
    }

internal class ManageProductsScreenViewModelImpl(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getAllProductCategoryUseCase: GetAllProductCategoryUseCase,
    private val addProductToDatabaseUseCase: AddProductToDatabaseUseCase,
    private val deleteProductFromDatabaseUseCase: DeleteProductFromDatabaseUseCase,
    private val updateProductToDatabaseUseCase: UpdateProductToDatabaseUseCase,
    private val addProductCategoryToDatabaseUseCase: AddProductCategoryToDatabaseUseCase,
    private val deleteProductCategoryFromDatabaseUseCase: DeleteProductCategoryFromDatabaseUseCase,
    private val updateProductCategoryToDatabaseUseCase: UpdateProductCategoryToDatabaseUseCase,
) : ManageProductsScreenViewModel() {

    init {
        query {
            getAllProductsUseCase()
                .map(ManageProductsViewState::ManageProductsSection)
        }

        query {
            getAllProductCategoryUseCase()
                .map(ManageProductsViewState::ManageProductCategorySection)
        }

    }

    override fun addProduct(product: Product) {
        runCommand {
            addProductToDatabaseUseCase(product)
        }

        query {
            getAllProductsUseCase()
                .map(ManageProductsViewState::ManageProductsSection)
                .onStart { delay(50) }
        }
    }

    override fun deleteProduct(productId: Int) {
        runCommand {
            deleteProductFromDatabaseUseCase(productId)
        }

        query {
            getAllProductsUseCase()
                .map(ManageProductsViewState::ManageProductsSection)
                .onStart { delay(50) }
        }
    }

    override fun updateProduct(product: Product) {
        runCommand {
            updateProductToDatabaseUseCase(product)
        }

        query {
            getAllProductsUseCase()
                .map(ManageProductsViewState::ManageProductsSection)
                .onStart { delay(50) }
        }
    }

    override fun addProductCategory(productCategory: ProductCategory) {
        runCommand {
            addProductCategoryToDatabaseUseCase(productCategory)
        }

        query {
            getAllProductCategoryUseCase()
                .map(ManageProductsViewState::ManageProductCategorySection)
                .onStart { delay(50) }
        }
    }

    override fun deleteProductCategory(productId: Int) {
        runCommand {
            deleteProductCategoryFromDatabaseUseCase(productId)
        }

        query {
            getAllProductCategoryUseCase()
                .map(ManageProductsViewState::ManageProductCategorySection)
                .onStart { delay(50) }
        }
    }

    override fun updateProductCategory(productCategory: ProductCategory) {
        runCommand {
            updateProductCategoryToDatabaseUseCase(productCategory)
        }

        query {
            getAllProductCategoryUseCase()
                .map(ManageProductsViewState::ManageProductCategorySection)
                .onStart { delay(50) }
        }
    }
}
