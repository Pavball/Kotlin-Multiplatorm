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
import androidx.compose.material.Checkbox
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
import domain.model.EmployeeDetails
import domain.model.WorkPosition
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.viewmodels.ManageEmployeesScreenViewModel
import ui.viewmodels.ManageEmployeesViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ManageEmployeesScreen() {
    val manageEmployeesScreenViewModel = koinViewModel<ManageEmployeesScreenViewModel>()

    val viewManageEmployeeState by manageEmployeesScreenViewModel.viewState<ManageEmployeesViewState.ManageEmployeesSection>()
        .collectAsState(initial = ManageEmployeesViewState.ManageEmployeesSection.initial)

    val viewManageWorkPositionsState by manageEmployeesScreenViewModel.viewState<ManageEmployeesViewState.ManageWorkPositionsSection>()
        .collectAsState(initial = ManageEmployeesViewState.ManageWorkPositionsSection.initial)

    val employees = viewManageEmployeeState.manageEmployees.filter { !it.isAdmin }
    val admins = viewManageEmployeeState.manageEmployees.filter { it.isAdmin }

    val workPositions = viewManageWorkPositionsState.manageWorkPositions

    // Separate search queries
    var employeeSearchQuery by remember { mutableStateOf("") }
    var adminSearchQuery by remember { mutableStateOf("") }

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedEmployee by remember { mutableStateOf<EmployeeDetails?>(null) }

    // Category Management State
    var showAddWorkPositionDialog by remember { mutableStateOf(false) }
    var selectedWorkPositionForEdit by remember { mutableStateOf<WorkPosition?>(null) }

    // Filtered lists based on respective search queries
    val filteredEmployees = employees.filter {
        it.name.contains(employeeSearchQuery, ignoreCase = true) || it.surname.contains(
            employeeSearchQuery,
            ignoreCase = true
        )
    }

    val filteredAdmins = admins.filter {
        it.name.contains(adminSearchQuery, ignoreCase = true) || it.surname.contains(
            adminSearchQuery,
            ignoreCase = true
        )
    }

    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("Manage Employees")
            Spacer(modifier = Modifier.height(8.dp))

            // Employee search bar
            TextField(
                value = employeeSearchQuery,
                onValueChange = { employeeSearchQuery = it },
                label = { Text("Search Employees by name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Divider(color = PopItTheme.colors.primary, thickness = 1.dp)

            // Employee list
            LazyColumn {
                items(filteredEmployees) { employee ->
                    EmployeeItem(
                        employee = employee,
                        onDelete = { manageEmployeesScreenViewModel.deleteEmployee(it) },
                        onEdit = { selectedEmployee = it }
                    )
                    Divider(color = PopItTheme.colors.primary, thickness = 1.dp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Add Employee", color = PopItTheme.colors.background)
            }
        }

        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("Manage Admins")
            Spacer(modifier = Modifier.height(8.dp))

            // Admin search bar
            TextField(
                value = adminSearchQuery,
                onValueChange = { adminSearchQuery = it },
                label = { Text("Search Admins by name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Divider()

            // Admin list
            LazyColumn {
                items(filteredAdmins) { admin ->
                    AdminItem(
                        admin = admin,
                        onDelete = { manageEmployeesScreenViewModel.deleteEmployee(it) },
                        onEdit = { selectedEmployee = it }
                    )
                    Divider(color = PopItTheme.colors.primary, thickness = 1.dp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Add Admin", color = PopItTheme.colors.background)
            }
        }

        // Category Management Column
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("Manage Work Positions", style = PopItTheme.typography.titleBold24)
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            // Category list
            LazyColumn {
                items(workPositions) { workPosition ->
                    WorkPositionItem(
                        workPosition = workPosition,
                        onDelete = { manageEmployeesScreenViewModel.deleteWorkPosition(it) },
                        onEdit = { selectedWorkPositionForEdit = it }
                    )

                    Divider(
                        color = PopItTheme.colors.primary,
                        thickness = 1.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showAddWorkPositionDialog = true },
                colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
            ) {
                Text("Add Work Position", color = PopItTheme.colors.background)
            }
        }
    }

    if (showAddDialog) {
        AddEmployeeDialog(
            workPositions = workPositions,
            onAddEmployee = { employee ->
                manageEmployeesScreenViewModel.addEmployee(employee)
            },
            onDismiss = { showAddDialog = false }
        )
    }

    selectedEmployee?.let {
        EditEmployeeDialog(
            workPositions = workPositions,
            employee = it,
            onUpdateEmployee = { updatedEmployee ->
                manageEmployeesScreenViewModel.updateEmployee(updatedEmployee)
                selectedEmployee = null
            },
            onDismiss = { selectedEmployee = null }
        )
    }

    if (showAddWorkPositionDialog) {
        AddWorkPositionDialog(
            onAddWorkPosition = { workPosition ->
                manageEmployeesScreenViewModel.addWorkPosition(workPosition)
            },
            onDismiss = { showAddWorkPositionDialog = false }
        )
    }

    selectedWorkPositionForEdit?.let {
        EditWorkPositionDialog(
            workPosition = it,
            onUpdateWorkPosition = { updatedWorkPosition ->
                manageEmployeesScreenViewModel.updateWorkPosition(updatedWorkPosition)
                selectedWorkPositionForEdit = null
            },
            onDismiss = { selectedWorkPositionForEdit = null }
        )
    }
}


@Composable
fun EmployeeItem(
    employee: EmployeeDetails,
    onDelete: (EmployeeDetails) -> Unit,
    onEdit: (EmployeeDetails) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete ${employee.name} ${employee.surname}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(employee)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
                ) {
                    Text("Delete", color = PopItTheme.colors.background)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
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
        Text("${employee.name} ${employee.surname} (${employee.phoneNumber})")
        Row {
            IconButton(onClick = { onEdit(employee) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Employee")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Employee")
            }
        }
    }
}

@Composable
fun AdminItem(
    admin: EmployeeDetails,
    onDelete: (EmployeeDetails) -> Unit,
    onEdit: (EmployeeDetails) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete ${admin.name} ${admin.surname}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(admin)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
                ) {
                    Text("Delete", color = PopItTheme.colors.background)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
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
        Text("${admin.name} ${admin.surname} (${admin.phoneNumber})")
        Row {
            IconButton(onClick = { onEdit(admin) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Admin")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Admin")
            }
        }
    }
}

@Composable
fun AddEmployeeDialog(
    workPositions: List<WorkPosition>,
    onAddEmployee: (EmployeeDetails) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    var selectedWorkPositionId by remember { mutableStateOf<Int?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val isFormValid by remember {
        derivedStateOf {
            name.isNotBlank() && surname.isNotBlank() &&
                    phoneNumber.isNotBlank() && email.isNotBlank() &&
                    password.isNotBlank() &&
                    (isAdmin || selectedWorkPositionId != null)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Employee") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (!isAdmin) {
                    Text("Select Work Position")
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(
                            onClick = { isDropdownExpanded = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                        ) {
                            Text(
                                text = selectedWorkPositionId?.let { id ->
                                    workPositions.find { it.positionId == id }?.positionName
                                        ?: "Select Work Position"
                                } ?: "Select Work Position",
                                color = PopItTheme.colors.background
                            )
                        }
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                        ) {
                            workPositions.forEach { position ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedWorkPositionId = position.positionId
                                        isDropdownExpanded = false
                                    }
                                ) {
                                    Text(position.positionName)
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Admin Role")
                    Checkbox(
                        checked = isAdmin,
                        onCheckedChange = { isAdmin = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val employee = EmployeeDetails(
                        employeeId = 0,
                        name = name,
                        surname = surname,
                        phoneNumber = phoneNumber,
                        email = email,
                        password = password,
                        workPositionId = if (!isAdmin) selectedWorkPositionId else null,
                        workPosition = if (!isAdmin) WorkPosition(0, "", "") else null,
                        isAdmin = isAdmin,
                        roleChanged = false
                    )
                    onAddEmployee(employee)
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
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Cancel", color = PopItTheme.colors.background)
            }
        }
    )
}


@Composable
fun EditEmployeeDialog(
    workPositions: List<WorkPosition>,
    employee: EmployeeDetails,
    onUpdateEmployee: (EmployeeDetails) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(employee.name) }
    var surname by remember { mutableStateOf(employee.surname) }
    var phoneNumber by remember { mutableStateOf(employee.phoneNumber) }
    var email by remember { mutableStateOf(employee.email) }
    var password by remember { mutableStateOf(employee.password) }
    var isAdmin by remember { mutableStateOf(employee.isAdmin) }
    var selectedWorkPositionId by remember { mutableStateOf(if (employee.isAdmin) null else employee.workPositionId) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var roleChanged by remember { mutableStateOf(false) }

    val isFormValid by remember {
        derivedStateOf {
            name.isNotBlank() && surname.isNotBlank() &&
                    phoneNumber.isNotBlank() && email.isNotBlank() &&
                    password.isNotBlank() &&
                    (isAdmin || selectedWorkPositionId != null)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Employee") },
        text = {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (!isAdmin) {
                    Text("Select Work Position")
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(
                            onClick = { isDropdownExpanded = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = PopItTheme.colors.primary)
                        ) {
                            Text(
                                text = selectedWorkPositionId?.let { id ->
                                    workPositions.find { it.positionId == id }?.positionName
                                        ?: "Select Work Position"
                                } ?: "Select Work Position",
                                color = PopItTheme.colors.background
                            )
                        }
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                        ) {
                            workPositions.forEach { position ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedWorkPositionId = position.positionId
                                        isDropdownExpanded = false
                                    }
                                ) {
                                    Text(position.positionName)
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Admin Role")
                    Checkbox(
                        checked = isAdmin,
                        onCheckedChange = {
                            roleChanged = (it != employee.isAdmin)
                            isAdmin = it
                        }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedEmployee = employee.copy(
                        name = name,
                        surname = surname,
                        phoneNumber = phoneNumber,
                        email = email,
                        password = password,
                        workPositionId = if (!isAdmin) selectedWorkPositionId else null,
                        isAdmin = isAdmin,
                        roleChanged = roleChanged
                    )
                    onUpdateEmployee(updatedEmployee)
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
                colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
            ) {
                Text("Cancel", color = PopItTheme.colors.background)
            }
        }
    )
}

@Composable
fun WorkPositionItem(
    workPosition: WorkPosition,
    onDelete: (Int) -> Unit,
    onEdit: (WorkPosition) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete the category ${workPosition.positionName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(workPosition.positionId)
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
        Text(workPosition.positionName)
        Row {
            IconButton(onClick = { onEdit(workPosition) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Work Position")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Work Position")
            }
        }
    }
}

@Composable
fun AddWorkPositionDialog(onAddWorkPosition: (WorkPosition) -> Unit, onDismiss: () -> Unit) {
    var positionName by remember { mutableStateOf("") }
    var positionDesc by remember { mutableStateOf("") }

    val isFormValid by remember {
        derivedStateOf {
            positionName.isNotBlank()
            positionDesc?.isNotBlank() ?: ""
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Work Position") },
        text = {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                TextField(
                    value = positionName,
                    onValueChange = { positionName = it },
                    label = { Text("Position Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = positionDesc,
                    onValueChange = { positionDesc = it },
                    label = { Text("Position Desc") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid as Boolean) {
                        onAddWorkPosition(
                            WorkPosition(
                                positionId = 0,
                                positionName = positionName,
                                positionDesc = positionDesc
                            )
                        )
                        onDismiss()
                    }
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
fun EditWorkPositionDialog(
    workPosition: WorkPosition,
    onUpdateWorkPosition: (WorkPosition) -> Unit,
    onDismiss: () -> Unit
) {
    var positionName by remember { mutableStateOf(workPosition.positionName) }
    var positionDesc by remember { mutableStateOf(workPosition.positionDesc) }

    val isFormValid by remember {
        derivedStateOf {
            positionName.isNotBlank()
            positionDesc?.isNotBlank() ?: ""
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Work Position") },
        text = {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                TextField(
                    value = positionName,
                    onValueChange = { positionName = it },
                    label = { Text("Position Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                positionDesc?.let {
                    TextField(
                        value = it,
                        onValueChange = { positionDesc = it },
                        label = { Text("Position Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid as Boolean) {
                        onUpdateWorkPosition(
                            workPosition.copy(
                                positionName = positionName,
                                positionDesc = positionDesc
                            )
                        )
                        onDismiss()
                    }
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
