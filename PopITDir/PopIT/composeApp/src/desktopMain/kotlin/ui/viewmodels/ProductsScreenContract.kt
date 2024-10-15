package ui.viewmodels

import domain.model.Product
import domain.model.ProductCategory
import domain.useCase.GetAllProductCategoryUseCase
import domain.useCase.GetAllProductsUseCase
import kotlinx.coroutines.flow.map

internal sealed class ProductsScreenViewState {

    data class ProductsSection(
        val products: List<Product>,
    ) : ProductsScreenViewState() {
        companion object {
            val initial = ProductsSection(
                emptyList()
            )
        }
    }

    data class ProductCategorySection(
        val productCategories: List<ProductCategory>,
    ) : ProductsScreenViewState() {
        companion object {
            val initial = ProductCategorySection(
                emptyList()
            )
        }
    }

}

internal abstract class ProductsScreenViewModel
    : BaseViewModel<ProductsScreenViewState>()

internal class ProductsScreenViewModelImpl(
    private val getProductsUseCase: GetAllProductsUseCase,
    private val getProductCategoriesUseCase: GetAllProductCategoryUseCase
) : ProductsScreenViewModel() {

    init {
        query {
            getProductsUseCase()
                .map(ProductsScreenViewState::ProductsSection)
        }

        query {
            getProductCategoriesUseCase()
                .map(ProductsScreenViewState::ProductCategorySection)
        }

    }
}
