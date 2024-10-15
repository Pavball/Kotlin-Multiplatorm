package ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appDesign.PopItTheme
import domain.model.Product
import domain.model.ProductCategory
import domain.model.ProductSortOption
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.viewmodels.ProductsScreenViewModel
import ui.viewmodels.ProductsScreenViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ProductsScreen(navController: NavController) {
    val productsScreenViewModel = koinViewModel<ProductsScreenViewModel>()

    val viewProductState by productsScreenViewModel.viewState<ProductsScreenViewState.ProductsSection>()
        .collectAsState(initial = ProductsScreenViewState.ProductsSection.initial)

    val viewProductCategoryState by productsScreenViewModel.viewState<ProductsScreenViewState.ProductCategorySection>()
        .collectAsState(initial = ProductsScreenViewState.ProductCategorySection.initial)

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Svi produkti") }
    var isCategoryExpanded by remember { mutableStateOf(false) }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }

    var productSortOption by remember { mutableStateOf(ProductSortOption.None) }
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }

    // Filter and sort products
    val filteredProducts = viewProductState.products
        .filter { product ->
            product.productName.contains(searchQuery, ignoreCase = true) &&
                    (selectedCategory == null || product.categoryId == selectedCategory?.categoryId)
        }
        .let { filteredList ->
            when (productSortOption) {
                ProductSortOption.Name -> filteredList.sortedBy { it.productName }
                ProductSortOption.Price -> filteredList.sortedBy { it.productPrice }
                ProductSortOption.Category -> filteredList.sortedBy { it.categoryId }
                ProductSortOption.None -> filteredList
            }
        }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PopItTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        top = PopItTheme.spacing.medium16,
                        start = PopItTheme.spacing.medium16,
                        end = PopItTheme.spacing.medium16,
                        bottom = 28.dp
                    )
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(PopItTheme.spacing.small8)
                        )
                        .padding(PopItTheme.spacing.small8),
                    singleLine = true
                ) { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(text = "Pretraži po imenu produkta...", color = PopItTheme.colors.primary)
                    }
                    innerTextField()
                }

                Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(
                            onClick = { isCategoryExpanded = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                        ) {
                            Text(
                                selectedCategory?.categoryName ?: "Select Category",
                                color = PopItTheme.colors.background
                            )
                        }

                        DropdownMenu(
                            expanded = isCategoryExpanded,
                            onDismissRequest = { isCategoryExpanded = false },
                        ) {
                            DropdownMenuItem(onClick = {
                                selectedCategory = null
                                isCategoryExpanded = false
                            }) {
                                Text("All Categories")
                            }
                            viewProductCategoryState.productCategories.forEach { category ->
                                DropdownMenuItem(onClick = {
                                    selectedCategory = category
                                    isCategoryExpanded = false
                                }) {
                                    Text(category.categoryName)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sort dropdown
                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(
                            onClick = { isSortDropdownExpanded = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                        ) {
                            productSortOption?.name?.let { Text(it, color = PopItTheme.colors.background) }
                        }
                        DropdownMenu(
                            expanded = isSortDropdownExpanded,
                            onDismissRequest = { isSortDropdownExpanded = false },
                        ) {
                            DropdownMenuItem(onClick = {
                                productSortOption = ProductSortOption.None
                                isSortDropdownExpanded = false
                            }) {
                                Text("None")
                            }
                            DropdownMenuItem(onClick = {
                                productSortOption = ProductSortOption.Name
                                isSortDropdownExpanded = false
                            }) {
                                Text("Name")
                            }
                            DropdownMenuItem(onClick = {
                                productSortOption = ProductSortOption.Price
                                isSortDropdownExpanded = false
                            }) {
                                Text("Price")
                            }
                            DropdownMenuItem(onClick = {
                                productSortOption = ProductSortOption.Category
                                isSortDropdownExpanded = false
                            }) {
                                Text("Category")
                            }
                        }
                    }
                }
            }
        }

        ProductSection(
            products = filteredProducts,
            onOrderClick = { productId ->
                navController.navigate("productDetails/$productId")
            }
        )

    }
}

@Composable
private fun ProductSection(
    products: List<Product>,
    onOrderClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize()) {
            Divider(
                color = PopItTheme.colors.background,
                thickness = 1.dp
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(PopItTheme.colors.primary)
                    .padding(PopItTheme.spacing.small8)
            ) {
                Text(
                    "Product ID",
                    modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Product Name",
                    modifier = modifier.weight(2f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Product Desc",
                    modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Product Price",
                    modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Product Stock",
                    modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )
            }

            ProductCardList(products, onOrderClick)
        }
    }
}

@Composable
internal fun ProductCardList(
    filteredAndSortedProducts: List<Product>,
    onOrderClick: (Int) -> Unit,
) {

    filteredAndSortedProducts.forEach { product ->
        Column {
            ProductCard(product = product,
                onClick = { onOrderClick(product.productId) }
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = PopItTheme.spacing.small8)
            .clickable { onClick() }
    ) {
        Text(
            product.productId.toString(),
            modifier = Modifier.weight(1f)
                .padding(start = PopItTheme.spacing.medium16)
        )

        Text(
            product.productName,
            modifier = Modifier.weight(2f)
        )
        Text(
            product.productDesc,
            modifier = Modifier.weight(1f)
        )

        Text(
            "${product.productPrice} €",
            modifier = Modifier.weight(1f)
        )

        Text(
            product.productStock.toString(),
            modifier = Modifier.weight(1f)
        )
    }
    Divider(
        color = Color.Gray,
        thickness = 1.dp
    )
}


