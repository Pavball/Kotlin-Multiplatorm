package ui.products

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import appDesign.PopItTheme
import domain.model.Product
import domain.model.ProductCategory
import domain.model.ProductSortOption
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.viewmodels.ManageProductsScreenViewModel
import ui.viewmodels.ManageProductsViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ManageProductsScreen(adminId: Int) {
    val manageProductsScreenViewModel = koinViewModel<ManageProductsScreenViewModel>()

    val viewManageProductsState by manageProductsScreenViewModel.viewState<ManageProductsViewState.ManageProductsSection>()
        .collectAsState(initial = ManageProductsViewState.ManageProductsSection.initial)

    val viewManageProductsCategoryState by manageProductsScreenViewModel.viewState<ManageProductsViewState.ManageProductCategorySection>()
        .collectAsState(initial = ManageProductsViewState.ManageProductCategorySection.initial)


    // Product Management State
    var searchQuery by remember { mutableStateOf("") }
    var productSortOption by remember { mutableStateOf(ProductSortOption.None) }
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }

    // Category Management State
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var selectedCategoryForEdit by remember { mutableStateOf<ProductCategory?>(null) }

    // Filter and sort products
    val filteredProducts = viewManageProductsState.manageProducts
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

    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Product Management Column
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("Manage Products", style = PopItTheme.typography.titleBold24)
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            // Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Category dropdown
            Box(modifier = Modifier.wrapContentSize()) {
                Button(
                    onClick = { isCategoryDropdownExpanded = true },
                    colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                ) {
                    Text(
                        selectedCategory?.categoryName ?: "Select Category",
                        color = PopItTheme.colors.background
                    )
                }

                DropdownMenu(
                    expanded = isCategoryDropdownExpanded,
                    onDismissRequest = { isCategoryDropdownExpanded = false },
                ) {
                    DropdownMenuItem(onClick = {
                        selectedCategory = null
                        isCategoryDropdownExpanded = false
                    }) {
                        Text("All Categories")
                    }
                    viewManageProductsCategoryState.manageProductCategories.forEach { category ->
                        DropdownMenuItem(onClick = {
                            selectedCategory = category
                            isCategoryDropdownExpanded = false
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

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                color = PopItTheme.colors.primary,
                thickness = 1.dp
            )

            // Product list
            LazyColumn {
                items(filteredProducts) { product ->
                    ProductItem(
                        product = product,
                        onDelete = { manageProductsScreenViewModel.deleteProduct(it) },
                        onEdit = { selectedProduct = it }
                    )

                    Divider(
                        color = PopItTheme.colors.primary,
                        thickness = 1.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showAddProductDialog = true },
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Add Product", color = PopItTheme.colors.background)
            }
        }

        // Category Management Column
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("Manage Product Categories", style = PopItTheme.typography.titleBold24)
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            // Category list
            LazyColumn {
                items(viewManageProductsCategoryState.manageProductCategories) { category ->
                    ProductCategoryItem(
                        category = category,
                        onDelete = { manageProductsScreenViewModel.deleteProductCategory(it) },
                        onEdit = { selectedCategoryForEdit = it }
                    )

                    Divider(
                        color = PopItTheme.colors.primary,
                        thickness = 1.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showAddCategoryDialog = true },
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Add Category", color = PopItTheme.colors.background)
            }
        }
    }

    // Dialogs for managing products and categories
    if (showAddProductDialog) {
        AddProductDialog(
            categories = viewManageProductsCategoryState.manageProductCategories,
            onAddProduct = { product ->
                manageProductsScreenViewModel.addProduct(product)
            },
            onDismiss = { showAddProductDialog = false },
            adminId = adminId
        )
    }

    selectedProduct?.let {
        EditProductDialog(
            product = it,
            categories = viewManageProductsCategoryState.manageProductCategories,
            onUpdateProduct = { updatedProduct ->
                manageProductsScreenViewModel.updateProduct(updatedProduct)
                selectedProduct = null
            },
            onDismiss = { selectedProduct = null },
            adminId = adminId
        )
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onAddCategory = { category ->
                manageProductsScreenViewModel.addProductCategory(category)
            },
            onDismiss = { showAddCategoryDialog = false }
        )
    }

    selectedCategoryForEdit?.let {
        EditCategoryDialog(
            category = it,
            onUpdateCategory = { updatedCategory ->
                manageProductsScreenViewModel.updateProductCategory(updatedCategory)
                selectedCategoryForEdit = null
            },
            onDismiss = { selectedCategoryForEdit = null }
        )
    }
}




@Composable
fun ProductItem(product: Product, onDelete: (Int) -> Unit, onEdit: (Product) -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete the product ${product.productName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(product.productId)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                ) {
                    Text("Delete", color = PopItTheme.colors.background)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                ) {
                    Text("Cancel", color = PopItTheme.colors.background)
                }
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${product.productName} - ${product.productPrice} €")
        Row {
            IconButton(onClick = { onEdit(product) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Product")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Product")
            }
        }
    }
}


@Composable
fun AddProductDialog(
    categories: List<ProductCategory>,
    adminId: Int,
    onDismiss: () -> Unit,
    onAddProduct: (Product) -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var productDesc by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productStock by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var productImage by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val isFormValid by remember {
        derivedStateOf {
            productName.isNotBlank() && productDesc.isNotBlank() &&
                    productPrice.toDoubleOrNull() != null &&
                    productStock.toIntOrNull() != null &&
                    selectedCategoryId != null &&
                    productImage.isNotBlank()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Product") },
        text = {
            Column {
                TextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productDesc,
                    onValueChange = { productDesc = it },
                    label = { Text("Product Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productPrice,
                    onValueChange = { productPrice = it },
                    label = { Text("Product Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productStock,
                    onValueChange = { productStock = it },
                    label = { Text("Product Stock") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Select Category")
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.wrapContentSize()) {
                    Button(
                        onClick = { isDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                    ) {
                        Text(
                            text = selectedCategoryId?.let { id ->
                                categories.find { it.categoryId == id }?.categoryName
                                    ?: "Select Category"
                            } ?: "Select Category",
                            color = PopItTheme.colors.background
                        )
                    }
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategoryId = category.categoryId
                                    isDropdownExpanded = false
                                }
                            ) {
                                Text(category.categoryName)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productImage,
                    onValueChange = { productImage = it },
                    label = { Text("Product Image URL") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedCategoryId?.let { categoryId ->
                        val productPriceValue = productPrice.toDoubleOrNull() ?: 0.0
                        val productStockValue = productStock.toIntOrNull() ?: 0
                        val product = Product(
                            0,
                            productName,
                            productDesc,
                            productPriceValue,
                            productStockValue,
                            productImage,
                            categoryId,
                            ProductCategory(0, "", ""),
                            createdByAdminId = adminId
                        )
                        onAddProduct(product)
                    }
                },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Add", color = PopItTheme.colors.background)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Cancel", color = PopItTheme.colors.background)
            }
        }
    )
}

@Composable
fun EditProductDialog(
    product: Product,
    categories: List<ProductCategory>,
    adminId: Int,
    onDismiss: () -> Unit,
    onUpdateProduct: (Product) -> Unit
) {
    var productName by remember { mutableStateOf(product.productName) }
    var productDesc by remember { mutableStateOf(product.productDesc ?: "") }
    var productPrice by remember { mutableStateOf(product.productPrice.toString()) }
    var productStock by remember { mutableStateOf(product.productStock.toString()) }
    var selectedCategoryId by remember { mutableStateOf(product.categoryId) }
    var productImage by remember { mutableStateOf(product.productImage ?: "") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val isFormValid by remember {
        derivedStateOf {
            productName.isNotBlank() && productDesc.isNotBlank() &&
                    productPrice.toDoubleOrNull() != null &&
                    productStock.toIntOrNull() != null && productImage.isNotBlank()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Product") },
        text = {
            Column {
                TextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productDesc,
                    onValueChange = { productDesc = it },
                    label = { Text("Product Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productPrice,
                    onValueChange = { productPrice = it },
                    label = { Text("Product Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productStock,
                    onValueChange = { productStock = it },
                    label = { Text("Product Stock") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Select Category")
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.wrapContentSize()) {
                    Button(
                        onClick = { isDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                    ) {
                        Text(
                            text = selectedCategoryId.let { id ->
                                categories.find { it.categoryId == id }?.categoryName
                                    ?: "Select Category"
                            },
                            color = PopItTheme.colors.background
                        )
                    }
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategoryId = category.categoryId
                                    isDropdownExpanded = false
                                }
                            ) {
                                Text(category.categoryName)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productImage,
                    onValueChange = { productImage = it },
                    label = { Text("Product Image URL") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        val productPriceValue = productPrice.toDoubleOrNull() ?: 0.0
                        val productStockValue = productStock.toIntOrNull() ?: 0
                        val updatedProduct = product.copy(
                            productName = productName,
                            productDesc = productDesc,
                            productPrice = productPriceValue,
                            productStock = productStockValue,
                            categoryId = selectedCategoryId,
                            productImage = productImage,
                            createdByAdminId = adminId
                        )
                        onUpdateProduct(updatedProduct)
                    }
                },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Update", color = PopItTheme.colors.background)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Cancel", color = PopItTheme.colors.background)
            }
        }
    )
}


@Composable
fun ProductCategoryItem(category: ProductCategory, onDelete: (Int) -> Unit, onEdit: (ProductCategory) -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete the category ${category.categoryName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(category.categoryId)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                ) {
                    Text("Delete", color = PopItTheme.colors.background)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                ) {
                    Text("Cancel", color = PopItTheme.colors.background)
                }
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(category.categoryName)
        Row {
            IconButton(onClick = { onEdit(category) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Category")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Category")
            }
        }
    }
}

@Composable
fun AddCategoryDialog(onAddCategory: (ProductCategory) -> Unit, onDismiss: () -> Unit) {
    var categoryName by remember { mutableStateOf("") }
    var categoryDesc by remember { mutableStateOf("") }
    val isFormValid by remember { derivedStateOf {
        categoryName.isNotBlank() &&
        categoryDesc.isNotBlank()
    } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Category") },
        text = {
            Column {
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = categoryDesc,
                    onValueChange = { categoryDesc = it },
                    label = { Text("Category Desc") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onAddCategory(
                            ProductCategory(
                                categoryId = 0,
                                categoryName = categoryName,
                                categoryDesc = categoryDesc
                            )
                        )
                        onDismiss()
                    }
                },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Add", color = PopItTheme.colors.background)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Cancel", color = PopItTheme.colors.background)
            }
        }
    )
}

@Composable
fun EditCategoryDialog(category: ProductCategory, onUpdateCategory: (ProductCategory) -> Unit, onDismiss: () -> Unit) {
    var categoryName by remember { mutableStateOf(category.categoryName) }
    var categoryDesc by remember { mutableStateOf(category.categoryDesc) }

    val isFormValid by remember { derivedStateOf {
        categoryName.isNotBlank() &&
        categoryDesc.isNotBlank()
    } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Category") },
        text = {
            Column {
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = categoryDesc,
                    onValueChange = { categoryDesc = it },
                    label = { Text("Category Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onUpdateCategory(
                            category.copy(
                                categoryName = categoryName,
                                categoryDesc = categoryDesc
                            )
                        )
                        onDismiss()
                    }
                },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Update", color = PopItTheme.colors.background)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Cancel", color = PopItTheme.colors.background)
            }
        }
    )
}



