package ui.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import domain.model.Service
import domain.model.ServiceSortOption
import domain.model.ServiceType
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.viewmodels.ManageServicesScreenViewModel
import ui.viewmodels.ManageServicesViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ManageServicesScreen(adminId: Int) {
    val manageServiceScreenViewModel = koinViewModel<ManageServicesScreenViewModel>()

    val viewManageServicesState by manageServiceScreenViewModel.viewState<ManageServicesViewState.ManageServicesSection>()
        .collectAsState(initial = ManageServicesViewState.ManageServicesSection.initial)

    val viewManageServiceTypesState by manageServiceScreenViewModel.viewState<ManageServicesViewState.ManageServiceTypesSection>()
        .collectAsState(initial = ManageServicesViewState.ManageServiceTypesSection.initial)

    val services = viewManageServicesState.manageServices
    val serviceTypes = viewManageServiceTypesState.manageServiceTypes

    var searchQuery by remember { mutableStateOf("") }
    var serviceSortOption by remember { mutableStateOf(ServiceSortOption.None) }
    var showAddServiceDialog by remember { mutableStateOf(false) }
    var showAddServiceTypeDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var selectedServiceType by remember { mutableStateOf<ServiceType?>(null) }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }

    // Filter and sort services
    val filteredServices = services
        .filter { service ->
            service.serviceName.contains(searchQuery, ignoreCase = true)
        }
        .let { filteredList ->
            when (serviceSortOption) {
                ServiceSortOption.None -> filteredList
                ServiceSortOption.Name -> filteredList.sortedBy { it.serviceName }
                ServiceSortOption.Price -> filteredList.sortedBy { it.servicePrice }
                ServiceSortOption.Service -> filteredList.sortedBy { it.serviceTypeId }
            }
        }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                Text("Manage Services", style = PopItTheme.typography.titleBold24)
                Spacer(modifier = Modifier.height(8.dp))

                // Search bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search by name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Sort dropdown
                Box(modifier = Modifier.wrapContentSize()) {
                    Button(
                        onClick = { isSortDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                    ) {
                        Text(serviceSortOption.name, color = PopItTheme.colors.background)
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

                Spacer(modifier = Modifier.height(8.dp))

                Divider(
                    color = PopItTheme.colors.primary,
                    thickness = 1.dp
                )

                // Service list
                LazyColumn {
                    items(filteredServices) { service ->
                        ServiceItem(
                            service = service,
                            onDelete = { manageServiceScreenViewModel.deleteService(it) },
                            onEdit = { selectedService = it }
                        )

                        Divider(
                            color = PopItTheme.colors.primary,
                            thickness = 1.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showAddServiceDialog = true },
                    colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                ) {
                    Text("Add Service", color = PopItTheme.colors.background)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                Text("Manage Service Types", style = PopItTheme.typography.titleBold24)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(serviceTypes) { serviceType ->
                        ServiceTypeItem(
                            serviceType = serviceType,
                            onDelete = { manageServiceScreenViewModel.deleteServiceType(it) },
                            onEdit = { selectedServiceType = it }
                        )

                        Divider(
                            color = PopItTheme.colors.primary,
                            thickness = 1.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showAddServiceTypeDialog = true },
                    colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                ) {
                    Text("Add Service Type", color = PopItTheme.colors.background)
                }
            }
        }
    }

    if (showAddServiceDialog) {
        AddServiceDialog(
            serviceTypes = serviceTypes,
            onAddService = { service ->
                manageServiceScreenViewModel.addService(service)
            },
            onDismiss = { showAddServiceDialog = false },
            adminId = adminId
        )
    }

    if (showAddServiceTypeDialog) {
        AddServiceTypeDialog(
            onAddServiceType = { serviceType ->
                manageServiceScreenViewModel.addServiceType(serviceType)
            },
            onDismiss = { showAddServiceTypeDialog = false }
        )
    }

    selectedService?.let {
        EditServiceDialog(
            service = it,
            serviceTypes = serviceTypes,
            onUpdateService = { updatedService ->
                manageServiceScreenViewModel.updateService(updatedService)
                selectedService = null
            },
            onDismiss = { selectedService = null },
            adminId = adminId
        )
    }

    selectedServiceType?.let {
        EditServiceTypeDialog(
            serviceType = it,
            onUpdateServiceType = { updatedServiceType ->
                manageServiceScreenViewModel.updateServiceType(updatedServiceType)
                selectedServiceType = null
            },
            onDismiss = { selectedServiceType = null }
        )
    }
}

@Composable
fun ServiceItem(service: Service, onDelete: (Int) -> Unit, onEdit: (Service) -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete the service ${service.serviceName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(service.serviceId)
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
        Text("${service.serviceName} - ${service.servicePrice} €")
        Row {
            IconButton(onClick = { onEdit(service) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Service")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Service")
            }
        }
    }
}

@Composable
fun AddServiceDialog(
    serviceTypes: List<ServiceType>,
    adminId: Int,
    onDismiss: () -> Unit,
    onAddService: (Service) -> Unit
) {
    var serviceName by remember { mutableStateOf("") }
    var serviceDesc by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }
    var selectedServiceTypeId by remember { mutableStateOf<Int?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val isFormValid by remember {
        derivedStateOf {
            serviceName.isNotBlank() && serviceDesc.isNotBlank() &&
                    servicePrice.toDoubleOrNull() != null &&
                    selectedServiceTypeId != null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Service") },
        text = {
            Column {
                TextField(
                    value = serviceName,
                    onValueChange = { serviceName = it },
                    label = { Text("Service Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = serviceDesc,
                    onValueChange = { serviceDesc = it },
                    label = { Text("Service Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = servicePrice,
                    onValueChange = { servicePrice = it },
                    label = { Text("Service Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Select Service Type")
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.wrapContentSize()) {
                    Button(
                        onClick = { isDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                    ) {
                        Text(
                            text = selectedServiceTypeId?.let { id ->
                                serviceTypes.find { it.typeId == id }?.typeName
                                    ?: "Select Type"
                            } ?: "Select Type",
                            color = PopItTheme.colors.background
                        )
                    }
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                    ) {
                        serviceTypes.forEach { type ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedServiceTypeId = type.typeId
                                    isDropdownExpanded = false
                                }
                            ) {
                                Text(type.typeName)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedServiceTypeId?.let { typeId ->
                        val servicePriceValue = servicePrice.toDoubleOrNull() ?: 0.0
                        val service = Service(
                            serviceId = 0,
                            serviceName = serviceName,
                            serviceDesc = serviceDesc,
                            servicePrice = servicePriceValue,
                            serviceTypeId = typeId,
                            serviceType = serviceTypes.first { it.typeId == typeId },
                            createdByAdminId = adminId
                        )
                        onAddService(service)
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
fun EditServiceDialog(
    service: Service,
    serviceTypes: List<ServiceType>,
    adminId: Int,
    onDismiss: () -> Unit,
    onUpdateService: (Service) -> Unit
) {
    var serviceName by remember { mutableStateOf(service.serviceName) }
    var serviceDesc by remember { mutableStateOf(service.serviceDesc ?: "") }
    var servicePrice by remember { mutableStateOf(service.servicePrice.toString()) }
    var selectedServiceTypeId by remember { mutableStateOf(service.serviceTypeId) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val isFormValid by remember {
        derivedStateOf {
            serviceName.isNotBlank() && serviceDesc.isNotBlank() &&
                    servicePrice.toDoubleOrNull() != null &&
                    selectedServiceTypeId != null
        }
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Service") },
        text = {
            Column {
                TextField(
                    value = serviceName,
                    onValueChange = { serviceName = it },
                    label = { Text("Service Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = serviceDesc,
                    onValueChange = { serviceDesc = it },
                    label = { Text("Service Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = servicePrice,
                    onValueChange = { servicePrice = it },
                    label = { Text("Service Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Select Service Type")
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.wrapContentSize()) {
                    Button(
                        onClick = { isDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                    ) {
                        Text(
                            text = selectedServiceTypeId?.let { id ->
                                serviceTypes.find { it.typeId == id }?.typeName
                                    ?: "Select Type"
                            } ?: "Select Type",
                            color = PopItTheme.colors.background
                        )
                    }
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                    ) {
                        serviceTypes.forEach { type ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedServiceTypeId = type.typeId
                                    isDropdownExpanded = false
                                }
                            ) {
                                Text(type.typeName)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val servicePriceValue = servicePrice.toDoubleOrNull() ?: 0.0
                    val updatedService = service.copy(
                        serviceName = serviceName,
                        serviceDesc = serviceDesc,
                        servicePrice = servicePriceValue,
                        serviceTypeId = selectedServiceTypeId,
                        serviceType = serviceTypes.first { it.typeId == selectedServiceTypeId },
                        createdByAdminId = adminId
                    )
                    onUpdateService(updatedService)
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
fun ServiceTypeItem(
    serviceType: ServiceType,
    onDelete: (Int) -> Unit,
    onEdit: (ServiceType) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete the service type ${serviceType.typeName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(serviceType.typeId)
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
        Text(serviceType.typeName)
        Row {
            IconButton(onClick = { onEdit(serviceType) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Service Type")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Service Type")
            }
        }
    }
}

@Composable
fun AddServiceTypeDialog(
    onDismiss: () -> Unit,
    onAddServiceType: (ServiceType) -> Unit
) {
    var typeName by remember { mutableStateOf("") }
    var typeDesc by remember { mutableStateOf("") }
    val isFormValid by remember { derivedStateOf {
        typeName.isNotBlank()
        typeDesc?.isNotBlank() ?: ""
    } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add Service Type",
                modifier = Modifier
                    .padding(bottom = PopItTheme.spacing.small8),
            )
        },
        text = {
            Column {
                TextField(
                    value = typeName,
                    onValueChange = { typeName = it },
                    label = { Text("Service Type Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = typeDesc,
                    onValueChange = { typeDesc = it },
                    label = { Text("Service Type Desc") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newServiceType = ServiceType(
                        typeId = 0,
                        typeName = typeName,
                        typeDesc = typeDesc
                    )
                    onAddServiceType(newServiceType)
                },
                enabled = isFormValid as Boolean,
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
fun EditServiceTypeDialog(
    serviceType: ServiceType,
    onDismiss: () -> Unit,
    onUpdateServiceType: (ServiceType) -> Unit
) {
    var typeName by remember { mutableStateOf(serviceType.typeName) }
    var typeDesc by remember { mutableStateOf(serviceType.typeDesc) }

    val isFormValid by remember { derivedStateOf {
        typeName.isNotBlank()
        typeDesc?.isNotBlank() ?: ""
    } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Service Type") },
        text = {
            Column {
                TextField(
                    value = typeName,
                    onValueChange = { typeName = it },
                    label = { Text("Service Type Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                typeDesc?.let {
                    TextField(
                        value = it,
                        onValueChange = { typeDesc = it },
                        label = { Text("Service Type Desc") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedServiceType = serviceType.copy(
                        typeName = typeName,
                        typeDesc = typeDesc
                    )
                    onUpdateServiceType(updatedServiceType)
                },
                enabled = isFormValid as Boolean,
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
