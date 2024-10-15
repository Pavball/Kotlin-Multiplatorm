package ui.services

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
import androidx.compose.material.TextButton
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
import domain.model.EmployeeDetails
import domain.model.Service
import domain.model.ServiceRequest
import domain.model.ServiceSortOption
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.viewmodels.ServicesScreenViewModel
import ui.viewmodels.ServicesScreenViewState


@OptIn(KoinExperimentalAPI::class)
@Composable
fun ServicesScreen(
    navController: NavController
) {

    val servicesScreenViewModel = koinViewModel<ServicesScreenViewModel>()

    val viewServiceRequestState by servicesScreenViewModel.viewState<ServicesScreenViewState.ServiceRequestsSection>()
        .collectAsState(initial = ServicesScreenViewState.ServiceRequestsSection.initial)

    val viewServiceState by servicesScreenViewModel.viewState<ServicesScreenViewState.ServicesSection>()
        .collectAsState(initial = ServicesScreenViewState.ServicesSection.initial)

    val viewEmployeeState by servicesScreenViewModel.viewState<ServicesScreenViewState.EmployeeSection>()
        .collectAsState(initial = ServicesScreenViewState.EmployeeSection.initial)

    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<Service?>(null) }
    var serviceSortOption by remember { mutableStateOf(ServiceSortOption.None) }

    var isSortDropdownExpanded by remember { mutableStateOf(false) }
    var isCategoryExpanded by remember { mutableStateOf(false) }

    // Filter and sort products
    val filteredServices = viewServiceRequestState.serviceRequests
        .filter { serviceRequest ->
            serviceRequest.userDetails.name.plus(serviceRequest.userDetails.surname).contains(searchQuery, ignoreCase = true) &&
                    (selectedType == null || serviceRequest.serviceId == selectedType?.serviceId)
        }
        .let { filteredList ->
            when (serviceSortOption) {
                ServiceSortOption.Name -> filteredList.sortedBy { it.service.serviceName }
                ServiceSortOption.Price -> filteredList.sortedBy { it.service.servicePrice }
                ServiceSortOption.Service -> filteredList.sortedBy { it.serviceId }
                ServiceSortOption.None -> filteredList
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
                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(
                            onClick = { isCategoryExpanded = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                        ) {
                            Text(
                                selectedType?.serviceName ?: "Select Service",
                                color = PopItTheme.colors.background
                            )
                        }

                        DropdownMenu(
                            expanded = isCategoryExpanded,
                            onDismissRequest = { isCategoryExpanded = false },
                        ) {
                            DropdownMenuItem(onClick = {
                                selectedType = null
                                isCategoryExpanded = false
                            }) {
                                Text("All Categories")
                            }
                            viewServiceState.services.forEach { service ->
                                DropdownMenuItem(onClick = {
                                    selectedType = service
                                    isCategoryExpanded = false
                                }) {
                                    Text(service.serviceName)
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
                            Text(
                                serviceSortOption.name,
                                color = PopItTheme.colors.background
                            )
                        }
                        DropdownMenu(
                            expanded = isSortDropdownExpanded,
                            onDismissRequest = { isSortDropdownExpanded = false },
                        ) {
                            DropdownMenuItem(onClick = {
                                serviceSortOption = ServiceSortOption.None
                                isSortDropdownExpanded = false
                            }) {
                                Text("None")
                            }
                            DropdownMenuItem(onClick = {
                                serviceSortOption = ServiceSortOption.Name
                                isSortDropdownExpanded = false
                            }) {
                                Text("Name")
                            }
                            DropdownMenuItem(onClick = {
                                serviceSortOption = ServiceSortOption.Price
                                isSortDropdownExpanded = false
                            }) {
                                Text("Price")
                            }
                            DropdownMenuItem(onClick = {
                                serviceSortOption = ServiceSortOption.Service
                                isSortDropdownExpanded = false
                            }) {
                                Text("Service")
                            }
                        }
                    }
                }
            }
        }

        ServiceRequestsSection(
            serviceRequests = filteredServices,
            onServiceClick = { requestId, serviceId ->
                navController.navigate("serviceRequestDetails/$requestId/$serviceId")
            },
            employees = viewEmployeeState.employees
        )

    }
}

@Composable
private fun ServiceRequestsSection(
    serviceRequests: List<ServiceRequest>,
    employees: List<EmployeeDetails>,
    onServiceClick: (Int, Int) -> Unit,
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
                    "Request ID",
                    modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "ID - Ime Prezime",
                    modifier = modifier.weight(2f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Service Name",
                    modifier = modifier.weight(2f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Status", modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Ukupno", modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Datum", modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )

                Text(
                    "Preuzeo Zaposlenik", modifier = modifier.weight(1f),
                    color = PopItTheme.colors.background
                )
            }

            ServiceRequestCardList(serviceRequests, employees, onServiceClick)
        }
    }
}

@Composable
internal fun ServiceRequestCardList(
    filteredAndSortedServiceRequests: List<ServiceRequest>,
    employees: List<EmployeeDetails>,
    onServiceClick: (Int, Int) -> Unit,
) {

    filteredAndSortedServiceRequests.forEach { serviceRequest ->
        Column {
            ServiceRequestCard(serviceRequest = serviceRequest,
                employees = employees,
                onClick = { onServiceClick(serviceRequest.requestId, serviceRequest.serviceId) }
            )
        }
    }
}

@Composable
fun ServiceRequestCard(
    serviceRequest: ServiceRequest,
    employees: List<EmployeeDetails>,
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
            serviceRequest.requestId.toString(),
            modifier = Modifier.weight(1f)
                .padding(start = PopItTheme.spacing.medium16)
        )

        Text(
            "${serviceRequest.userId} - ${serviceRequest.userDetails.name} " + serviceRequest.userDetails.surname,
            modifier = Modifier.weight(2f)
        )

        Text(
            "${serviceRequest.service.serviceName}",
            modifier = Modifier.weight(2f)
        )

        Text(
            serviceRequest.requestStatus,
            modifier = Modifier.weight(1f)
        )

        Text(
            "${serviceRequest.service.servicePrice} €",
            modifier = Modifier.weight(1f)
        )

        Text(
            serviceRequest.requestDate,
            modifier = Modifier.weight(1f)
        )

        val employee = employees.find { it.employeeId == serviceRequest.employeeId }

        Text(
            if (serviceRequest.employeeId == 0) {
                "Nema Zaposlenika"
            } else {
                serviceRequest.employeeId.toString().plus(
                    employee?.let { " - ${it.name} ${it.surname}" } ?: ""
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
    Divider(
        color = Color.Gray,
        thickness = 1.dp
    )
}

