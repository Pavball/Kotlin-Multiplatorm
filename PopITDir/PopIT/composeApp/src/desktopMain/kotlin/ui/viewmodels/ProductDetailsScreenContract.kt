package ui.viewmodels

import domain.model.Product
import domain.model.ProductCategory
import domain.useCase.GetProductDetailsUseCase
import kotlinx.coroutines.flow.map

internal sealed class ProductDetailsScreenViewState {

    data class ProductDetailsSection(
        val productDetail: Product,
    ) : ProductDetailsScreenViewState() {
        companion object {
            val initial = ProductDetailsSection(
                Product(
                    0,
                    "",
                    "",
                    0.0,
                    0,
                    "",
                    0,
                    ProductCategory(0, "", ""),
                    0
                )
            )
        }
    }
}

internal abstract class ProductDetailsScreenViewModel
    : BaseViewModel<ProductDetailsScreenViewState>()

internal class ProductDetailsScreenViewModelImpl(
    private val productId: Int,
    private val getProductDetailsUseCase: GetProductDetailsUseCase

) : ProductDetailsScreenViewModel() {

    init {
        query {
            getProductDetailsUseCase(productId)
                .map(ProductDetailsScreenViewState::ProductDetailsSection)
        }
    }
}
