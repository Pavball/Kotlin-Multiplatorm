package ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appDesign.PopItTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import ui.viewmodels.OrderDetailsScreenViewModel
import ui.viewmodels.OrderDetailsScreenViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun OrderDetailsScreen(
    navController: NavController,
    orderId: Int
) {
    val ordersDetailsScreenViewModel = koinViewModel<OrderDetailsScreenViewModel> {
        parametersOf(orderId)
    }

    val viewOrderDetailsState by ordersDetailsScreenViewModel.viewState<OrderDetailsScreenViewState.OrderDetailsSection>()
        .collectAsState(initial = OrderDetailsScreenViewState.OrderDetailsSection.initial)

    var showCompleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(PopItTheme.spacing.medium16)
            .fillMaxSize()
    ) {
        // Order Info Section
        Column(
            modifier = Modifier
                .padding(PopItTheme.spacing.medium16)
                .background(PopItTheme.colors.primary)
                .padding(PopItTheme.spacing.medium16)
                .fillMaxWidth()
        ) {
            Text(
                text = "Order ID: ${viewOrderDetailsState.orderDetail.orderId}",
                style = PopItTheme.typography.titleBold24,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))
            Text(
                text = "User: ${viewOrderDetailsState.orderDetail.userDetails.name} " +
                        viewOrderDetailsState.orderDetail.userDetails.surname,
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Text(
                text = "Status: ${viewOrderDetailsState.orderDetail.orderStatus}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Text(
                text = "Total: ${viewOrderDetailsState.orderDetail.totalAmount} €",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Text(
                text = "Date: ${viewOrderDetailsState.orderDetail.orderDate}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))
            Text(
                text = "Address: ${viewOrderDetailsState.orderDetail.userDetails.streetNo}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Text(
                text = "Postal Code: ${viewOrderDetailsState.orderDetail.userDetails.postalCode}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Text(
                text = "Phone Number: ${viewOrderDetailsState.orderDetail.userDetails.phoneNumber}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
        }

        Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

        // Order Details Section
        Column(
            modifier = Modifier
                .padding(PopItTheme.spacing.medium16)
                .background(PopItTheme.colors.primary)
                .padding(PopItTheme.spacing.medium16)
                .fillMaxWidth()
        ) {
            Text(
                text = "Order Details",
                style = PopItTheme.typography.titleBold24,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            viewOrderDetailsState.orderDetail.orderDetails.forEach { detail ->
                Column(
                    modifier = Modifier
                        .padding(vertical = PopItTheme.spacing.small8)
                ) {
                    Text(
                        text = "${detail.productName} - ${detail.orderQuantity} pcs @ €${detail.unitPrice}",
                        style = PopItTheme.typography.button16,
                        color = PopItTheme.colors.background
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

        // Complete Order Button
        if(viewOrderDetailsState.orderDetail.orderStatus != "Completed"){
        TextButton(
            onClick = { showCompleteDialog = true },
            colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
        ) {
            Text(text = "Complete Order", color = PopItTheme.colors.background)
        }
        }

        // Dialog for confirming order completion
        if (showCompleteDialog) {
            AlertDialog(
                onDismissRequest = { showCompleteDialog = false },
                title = { Text("Complete Order") },
                text = { Text("Are you sure you want to mark this order as completed?") },
                confirmButton = {
                    TextButton(onClick = {
                        ordersDetailsScreenViewModel.completeOrder(orderId)
                        showCompleteDialog = false
                        navController.navigateUp()  // Navigate back after completion
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCompleteDialog = false }) {
                        Text("No")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

        TextButton(
            onClick = { navController.navigateUp() },
            colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
        ) {
            Text(text = "Back to Orders", color = PopItTheme.colors.background)
        }
    }
}
