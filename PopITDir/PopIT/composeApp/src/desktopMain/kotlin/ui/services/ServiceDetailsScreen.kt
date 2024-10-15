package ui.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appDesign.PopItTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import ui.viewmodels.ServiceRequestDetailsScreenViewModel
import ui.viewmodels.ServiceRequestDetailsScreenViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ServiceDetailsScreen(
    navController: NavController,
    requestId: Int,
    employeeId: Int,
    serviceId: Int,
    employeeName: String
) {
    val serviceRequestDetailsScreenViewModel = koinViewModel<ServiceRequestDetailsScreenViewModel> {
        parametersOf(requestId, employeeId, serviceId)
    }

    val viewServiceDetailsState by serviceRequestDetailsScreenViewModel.viewState<ServiceRequestDetailsScreenViewState.ServiceRequestDetailsSection>()
        .collectAsState(initial = ServiceRequestDetailsScreenViewState.ServiceRequestDetailsSection.initial)

    val viewIsEmployeeAuthorizedState by serviceRequestDetailsScreenViewModel.viewState<ServiceRequestDetailsScreenViewState.IsEmployeeAuthorizedSection>()
        .collectAsState(initial = ServiceRequestDetailsScreenViewState.IsEmployeeAuthorizedSection.initial)

    var newMessage by remember { mutableStateOf("") }
    var finishMessage by remember { mutableStateOf("Hvala Vam na javljanju! Service je zatvoren!") }
    var showFinishDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val isServiceTaken = viewServiceDetailsState.serviceDetail.employeeId != 0

    Column(
        modifier = Modifier
            .padding(PopItTheme.spacing.medium16)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Show service assign option if not taken and authorized
        if (!isServiceTaken && viewIsEmployeeAuthorizedState.isAuthorized) {
            Button(
                onClick = {
                    serviceRequestDetailsScreenViewModel.assignServiceToEmployee(
                        requestId,
                        employeeId
                    )
                },
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Take Service", color = PopItTheme.colors.background)
            }
            TextButton(onClick = { navController.navigateUp() }) {
                Text(text = "Back to Services")
            }
        }

        // Show unauthorized message if not taken and not authorized
        if (!isServiceTaken && !viewIsEmployeeAuthorizedState.isAuthorized) {
            Text(
                text = "Nemate ovlasti da pristupite ovome servisu!",
                style = PopItTheme.typography.titleBold24,
                color = PopItTheme.colors.primary
            )
            TextButton(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text(text = "Back to Services", color = PopItTheme.colors.background)
            }
        }

        // Show service details if taken by the current employee
        if (employeeId == viewServiceDetailsState.serviceDetail.employeeId) {
            Column(
                modifier = Modifier
                    .padding(PopItTheme.spacing.medium16)
                    .background(PopItTheme.colors.primary)
                    .padding(PopItTheme.spacing.medium16)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Request ID: ${viewServiceDetailsState.serviceDetail.requestId}",
                    style = PopItTheme.typography.titleBold24,
                    color = PopItTheme.colors.background
                )
                Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))
                Text(
                    text = "User: ${viewServiceDetailsState.serviceDetail.userDetails.name} " +
                            viewServiceDetailsState.serviceDetail.userDetails.surname,
                    style = PopItTheme.typography.button16,
                    color = PopItTheme.colors.background
                )
                Text(
                    text = "Status: ${viewServiceDetailsState.serviceDetail.requestStatus}",
                    style = PopItTheme.typography.button16,
                    color = PopItTheme.colors.background
                )
                Text(
                    text = "Total: ${viewServiceDetailsState.serviceDetail.service.servicePrice} €",
                    style = PopItTheme.typography.button16,
                    color = PopItTheme.colors.background
                )
                Text(
                    text = "Date: ${viewServiceDetailsState.serviceDetail.requestDate}",
                    style = PopItTheme.typography.button16,
                    color = PopItTheme.colors.background
                )
                Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))
                Text(
                    text = "Address: ${viewServiceDetailsState.serviceDetail.userDetails.streetNo}",
                    style = PopItTheme.typography.button16,
                    color = PopItTheme.colors.background
                )
                Text(
                    text = "Postal Code: ${viewServiceDetailsState.serviceDetail.userDetails.postalCode}",
                    style = PopItTheme.typography.button16,
                    color = PopItTheme.colors.background
                )
                Text(
                    text = "Phone Number: ${viewServiceDetailsState.serviceDetail.userDetails.phoneNumber}",
                    style = PopItTheme.typography.button16,
                    color = PopItTheme.colors.background
                )
            }

            Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

            // Service Details Section
            Column(
                modifier = Modifier
                    .padding(PopItTheme.spacing.medium16)
                    .background(PopItTheme.colors.primary)
                    .padding(PopItTheme.spacing.medium16)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Service Details",
                    style = PopItTheme.typography.titleBold24,
                    color = PopItTheme.colors.background
                )
                Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

                Column(
                    modifier = Modifier
                        .padding(vertical = PopItTheme.spacing.small8)
                ) {
                    Text(
                        text = "Tip servisa: ${viewServiceDetailsState.serviceDetail.service.serviceType.typeName}",
                        style = PopItTheme.typography.button16,
                        color = PopItTheme.colors.background
                    )

                    Text(
                        text = "Opis servisa: ${viewServiceDetailsState.serviceDetail.service.serviceType.typeDesc}",
                        style = PopItTheme.typography.button16,
                        color = PopItTheme.colors.background
                    )
                }
                Divider(color = PopItTheme.colors.background, thickness = 1.dp)
            }

            Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

            // Communication Section
            Column(
                modifier = Modifier
                    .padding(PopItTheme.spacing.medium16)
                    .background(PopItTheme.colors.primary)
                    .padding(PopItTheme.spacing.medium16)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Communication",
                    style = PopItTheme.typography.titleBold24,
                    color = PopItTheme.colors.background
                )
                Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    value = viewServiceDetailsState.serviceDetail.communication,
                    onValueChange = {},
                    singleLine = false,
                    readOnly = true
                )


                Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

                if (viewServiceDetailsState.serviceDetail.requestStatus != "Completed") {

                    BasicTextField(
                        value = newMessage,
                        onValueChange = { newMessage = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        singleLine = false,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        )
                    )


                    Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

                    TextButton(onClick = {
                        val updatedCommunication =
                            "${viewServiceDetailsState.serviceDetail.communication}\n$employeeName: $newMessage"
                        serviceRequestDetailsScreenViewModel.sendMessageToServiceRequest(
                            requestId,
                            updatedCommunication
                        )
                        newMessage = ""
                    }) {
                        Text("Send Message", color = PopItTheme.colors.background)
                    }
                }
                Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(
                        onClick = { navController.navigateUp() },
                        colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
                    ) {
                        Text(text = "Back to Services", color = PopItTheme.colors.background)
                    }

                    if (viewServiceDetailsState.serviceDetail.requestStatus != "Completed") {
                        // Finish Request Button
                        TextButton(onClick = { showFinishDialog = true }) {
                            Text("Finish Request", color = PopItTheme.colors.background)
                        }
                    } else {
                        TextButton(onClick = { navController.navigateUp() }) {
                            Text("Request Finished", color = PopItTheme.colors.background)
                        }
                    }
                }

                // Dialog for confirming request completion
                if (showFinishDialog) {
                    AlertDialog(
                        onDismissRequest = { showFinishDialog = false },
                        title = { Text("Finish Request") },
                        text = { Text("Are you sure you want to finish this service request?") },
                        confirmButton = {
                            TextButton(onClick = {
                                val finishedCommunication =
                                    "${viewServiceDetailsState.serviceDetail.communication}\n$employeeName: $finishMessage"
                                serviceRequestDetailsScreenViewModel.finishServiceRequest(
                                    requestId,
                                    finishedCommunication
                                )
                                showFinishDialog = false
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showFinishDialog = false }) {
                                Text("No")
                            }
                        }
                    )
                }
            }
        }

        // Show message if someone else has taken the service
        if (isServiceTaken && employeeId != viewServiceDetailsState.serviceDetail.employeeId) {
            Text(
                text = "Netko drugi je preuzeo ovaj servis!",
                style = PopItTheme.typography.titleBold24,
                color = PopItTheme.colors.primary
            )
            TextButton(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text(text = "Back to Services", color = PopItTheme.colors.background)
            }
        }
    }


}

