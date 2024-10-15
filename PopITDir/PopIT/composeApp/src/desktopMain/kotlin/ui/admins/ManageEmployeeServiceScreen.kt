package ui.admins

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import appDesign.PopItTheme
import domain.model.EmployeeDetails
import domain.model.EmployeeService
import domain.model.Service
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.viewmodels.ManageEmployeeServiceScreenViewModel
import ui.viewmodels.ManageEmployeeServicesViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ManageEmployeeServicesScreen() {
    val manageEmployeeServicesViewModel = koinViewModel<ManageEmployeeServiceScreenViewModel>()

    val viewManageEmployeeServicesState by manageEmployeeServicesViewModel.viewState<ManageEmployeeServicesViewState.ManageEmployeeServiceSection>()
        .collectAsState(initial = ManageEmployeeServicesViewState.ManageEmployeeServiceSection.initial)

    val viewEmployeesState by manageEmployeeServicesViewModel.viewState<ManageEmployeeServicesViewState.ManageEmployeesSection>()
        .collectAsState(initial = ManageEmployeeServicesViewState.ManageEmployeesSection.initial)

    val viewServicesState by manageEmployeeServicesViewModel.viewState<ManageEmployeeServicesViewState.ManageServicesSection>()
        .collectAsState(initial = ManageEmployeeServicesViewState.ManageServicesSection.initial)


    // Držanje stanja za filtriranje i sortiranje
    var searchQuery by remember { mutableStateOf("") }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }
    var showAddEmployeeServiceDialog by remember { mutableStateOf(false) }
    var selectedEmployeeService by remember { mutableStateOf<EmployeeService?>(null) }

    // Filtriranje i sortiranje
    val filteredEmployeeServices = viewManageEmployeeServicesState.manageEmployeeServices
        .filter { employeeService ->
            employeeService.employee.name.contains(searchQuery, ignoreCase = true) ||
                    employeeService.service.serviceName.contains(searchQuery, ignoreCase = true)
        }
        .sortedBy { it.employee.name }

    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Upravljanje vezama između zaposlenika i usluga
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("Manage Employee Services", style = PopItTheme.typography.titleBold24)
            Spacer(modifier = Modifier.height(8.dp))

            // Pretraga
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by employee or service") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Sort Dropdown
            Box(modifier = Modifier.wrapContentSize()) {
                Button(
                    onClick = { isSortDropdownExpanded = true },
                    colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
                ) {
                    Text("Sort Options", color = PopItTheme.colors.background)
                }
                DropdownMenu(
                    expanded = isSortDropdownExpanded,
                    onDismissRequest = { isSortDropdownExpanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        // Sortiraj po imenu zaposlenika
                        filteredEmployeeServices.sortedBy { it.employee.name }
                        isSortDropdownExpanded = false
                    }) {
                        Text("Sort by Employee")
                    }
                    DropdownMenuItem(onClick = {
                        // Sortiraj po imenu usluge
                        filteredEmployeeServices.sortedBy { it.service.serviceName }
                        isSortDropdownExpanded = false
                    }) {
                        Text("Sort by Service")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lista veza zaposlenika i usluga
            LazyColumn {
                items(filteredEmployeeServices) { employeeService ->
                    EmployeeServiceItem(
                        employeeService = employeeService,
                        onDelete = { manageEmployeeServicesViewModel.deleteEmployeeService(it) },
                        onEdit = { selectedEmployeeService = it }
                    )

                    Divider(
                        color = PopItTheme.colors.primary,
                        thickness = 1.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Dugme za dodavanje nove veze
            Button(
                onClick = { showAddEmployeeServiceDialog = true },
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Add Employee Service", color = PopItTheme.colors.background)
            }
        }
    }

    // Dialog za dodavanje ili uređivanje
    if (showAddEmployeeServiceDialog) {
        AddEmployeeServiceDialog(
            employees = viewEmployeesState.employees,
            services = viewServicesState.services,
            onAddEmployeeService = { employeeService ->
                manageEmployeeServicesViewModel.addEmployeeService(employeeService)
            },
            onDismiss = { showAddEmployeeServiceDialog = false },
        )
    }

    selectedEmployeeService?.let {
        EditEmployeeServiceDialog(
            employeeService = it,
            services = viewServicesState.services,
            onUpdateEmployeeService = { updatedEmployeeService ->
                manageEmployeeServicesViewModel.updateEmployeeService(updatedEmployeeService)
                selectedEmployeeService = null
            },
            onDismiss = { selectedEmployeeService = null }
        )
    }
}

@Composable
fun EmployeeServiceItem(
    employeeService: EmployeeService,
    onDelete: (EmployeeService) -> Unit,
    onEdit: (EmployeeService) -> Unit
) {
    // State to control the visibility of the confirmation dialog
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Employee: ${employeeService.employee.name} ${employeeService.employee.surname}")
            Text("Service: ${employeeService.service.serviceName}")
        }
        Row {
            IconButton(onClick = { onEdit(employeeService) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { showDeleteConfirmationDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }

    // Show the confirmation dialog when showDeleteConfirmationDialog is true
    if (showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this employee service?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(employeeService)
                        showDeleteConfirmationDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmationDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}


@Composable
fun AddEmployeeServiceDialog(
    employees: List<EmployeeDetails>,
    services: List<Service>,
    onAddEmployeeService: (EmployeeService) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedEmployee by remember { mutableStateOf<EmployeeDetails?>(null) }
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var isServiceDropdownExpanded by remember { mutableStateOf(false) }
    var isEmployeeDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Employee Service") },
        text = {
            Column {
                // Dropdown for Services
                Text("Select Service")
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { isServiceDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                    ) {
                        Text(
                            text = selectedService?.serviceName ?: "Select Service",
                            color = PopItTheme.colors.background
                        )
                    }
                    DropdownMenu(
                        expanded = isServiceDropdownExpanded,
                        onDismissRequest = { isServiceDropdownExpanded = false }
                    ) {
                        services.forEach { service ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedService = service
                                    isServiceDropdownExpanded = false
                                }
                            ) {
                                Text(service.serviceName)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown for Employees
                Text("Select Employee")
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { isEmployeeDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                    ) {
                        Text(
                            text = selectedEmployee?.name?.plus(" ")
                                ?.plus(selectedEmployee?.surname) ?: "Select Employee",
                            color = PopItTheme.colors.background
                        )
                    }
                    DropdownMenu(
                        expanded = isEmployeeDropdownExpanded,
                        onDismissRequest = { isEmployeeDropdownExpanded = false }
                    ) {
                        employees.forEach { employee ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedEmployee = employee
                                    isEmployeeDropdownExpanded = false
                                }
                            ) {
                                Text("${employee.name} ${employee.surname}")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedEmployee != null && selectedService != null) {
                        val newEmployeeService = EmployeeService(
                            employeeServiceId = 0, // Auto-generated by the DB
                            employeeId = selectedEmployee!!.employeeId,
                            serviceId = selectedService!!.serviceId,
                            service = selectedService!!,
                            employee = selectedEmployee!!
                        )
                        onAddEmployeeService(newEmployeeService)
                        onDismiss()
                    }
                },
                enabled = selectedEmployee != null && selectedService != null,
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Add", color = PopItTheme.colors.background)
            }
        },
        dismissButton = {
            Button(
                onDismiss,
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Cancel", color = PopItTheme.colors.background)
            }
        }
    )
}


@Composable
fun EditEmployeeServiceDialog(
    employeeService: EmployeeService,
    services: List<Service>,
    onUpdateEmployeeService: (EmployeeService) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedService by remember { mutableStateOf(employeeService.service) }
    var isServiceDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit Employee Service")
        },
        text = {
            Column {
                // Show the employee name, but make it uneditable
                Text("Employee: ${employeeService.employee.name} ${employeeService.employee.surname}")
                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown for Services
                Text("Select Service")
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { isServiceDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                    ) {
                        Text(
                            text = selectedService.serviceName,
                            color = PopItTheme.colors.background
                        )
                    }
                    DropdownMenu(
                        expanded = isServiceDropdownExpanded,
                        onDismissRequest = { isServiceDropdownExpanded = false }
                    ) {
                        services.forEach { service ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedService = service
                                    isServiceDropdownExpanded = false
                                }
                            ) {
                                Text(service.serviceName)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedEmployeeService = employeeService.copy(
                        service = selectedService,
                        serviceId = selectedService.serviceId
                    )
                    onUpdateEmployeeService(updatedEmployeeService)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary),
                enabled = true
            ) {
                Text("Update", color = PopItTheme.colors.background)
            }
        },
        dismissButton = {
            Button(
                onDismiss,
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Cancel", color = PopItTheme.colors.background)
            }
        }
    )
}
