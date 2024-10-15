package ui.orders

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
import domain.model.Order
import domain.model.OrderSortOption
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.viewmodels.OrdersScreenViewModel
import ui.viewmodels.OrdersScreenViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun OrdersScreen(navController: NavController) {

    val ordersScreenViewModel = koinViewModel<OrdersScreenViewModel>()

    val viewOrderState by ordersScreenViewModel.viewState<OrdersScreenViewState.OrdersSection>()
        .collectAsState(initial = OrdersScreenViewState.OrdersSection.initial)

    var searchQuery by remember { mutableStateOf("") }
    var orderSortOption by remember { mutableStateOf(OrderSortOption.None) }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }

    // Filter and sort products
    val filteredOrders = viewOrderState.orders
        .filter { order ->
            order.userDetails.name.plus(order.userDetails.surname)
                .contains(searchQuery, ignoreCase = true)
        }
        .let { filteredList ->
            when (orderSortOption) {
                OrderSortOption.Price -> filteredList.sortedBy { it.totalAmount }
                OrderSortOption.Status -> filteredList.sortedBy { it.orderStatus }
                OrderSortOption.None -> filteredList
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
                        Text(text = "Pretraži po imenu osobe...", color = PopItTheme.colors.primary)
                    }
                    innerTextField()
                }

                Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Sort dropdown
                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(
                            onClick = { isSortDropdownExpanded = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                        ) {
                            Text(
                                orderSortOption.name,
                                color = PopItTheme.colors.background
                            )
                        }
                        DropdownMenu(
                            expanded = isSortDropdownExpanded,
                            onDismissRequest = { isSortDropdownExpanded = false },
                        ) {
                            DropdownMenuItem(onClick = {
                                orderSortOption = OrderSortOption.None
                                isSortDropdownExpanded = false
                            }) {
                                Text("None")
                            }
                            DropdownMenuItem(onClick = {
                                orderSortOption = OrderSortOption.Price
                                isSortDropdownExpanded = false
                            }) {
                                Text("Price")
                            }
                            DropdownMenuItem(onClick = {
                                orderSortOption = OrderSortOption.Status
                                isSortDropdownExpanded = false
                            }) {
                                Text("Status")
                            }
                        }
                    }
                }
            }
        }

        OrderSection(
            orders = filteredOrders,
            onOrderClick = { orderId ->
                navController.navigate("orderDetails/$orderId")
            }
        )

    }
}

@Composable
private fun OrderSection(
    orders: List<Order>,
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
                    "Order ID",
                    modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )
                Text(
                    "ID - Ime Prezime",
                    modifier = modifier.weight(2f),
                    color = PopItTheme.colors.background
                )
                Text("Status", modifier = modifier.weight(1f), color = PopItTheme.colors.background)

                Text("Ukupno", modifier = modifier.weight(1f), color = PopItTheme.colors.background)

                Text("Datum", modifier = modifier.weight(1f), color = PopItTheme.colors.background)
            }

            OrderCardList(orders, onOrderClick)
        }
    }
}

@Composable
internal fun OrderCardList(
    filteredAndSortedOrders: List<Order>,
    onOrderClick: (Int) -> Unit,
) {

    filteredAndSortedOrders.forEach { order ->
        Column {
            OrderCard(order = order,
                onClick = { onOrderClick(order.orderId) }
            )
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
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
            order.orderId.toString(),
            modifier = Modifier.weight(1f)
                .padding(start = PopItTheme.spacing.medium16)
        )

        Text(
            "${order.userId} - ${order.userDetails.name} " + order.userDetails.surname,
            modifier = Modifier.weight(2f)
        )

        Text(
            order.orderStatus,
            modifier = Modifier.weight(1f)
        )

        Text(
            "${order.totalAmount} €",
            modifier = Modifier.weight(1f)
        )

        Text(
            order.orderDate,
            modifier = Modifier.weight(1f)
        )
    }
    Divider(
        color = Color.Gray,
        thickness = 1.dp
    )
}