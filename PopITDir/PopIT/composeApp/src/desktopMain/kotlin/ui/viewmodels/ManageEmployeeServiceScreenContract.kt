package ui.viewmodels

import domain.model.EmployeeDetails
import domain.model.EmployeeService
import domain.model.Service
import domain.useCase.AddEmployeeSeviceToDatabaseUseCase
import domain.useCase.DeleteEmployeeServiceFromDatabaseUseCase
import domain.useCase.GetAllEmployeeServicesUseCase
import domain.useCase.GetAllEmployeesUseCase
import domain.useCase.GetAllServicesUseCase
import domain.useCase.UpdateEmployeeServiceToDatabaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal sealed class ManageEmployeeServicesViewState {

    data class ManageEmployeeServiceSection(
        val manageEmployeeServices: List<EmployeeService>,
    ) : ManageEmployeeServicesViewState() {
        companion object {
            val initial = ManageEmployeeServiceSection(
                emptyList()
            )
        }
    }

    data class ManageEmployeesSection(
        val employees: List<EmployeeDetails>,
    ) : ManageEmployeeServicesViewState() {
        companion object {
            val initial = ManageEmployeesSection(
                emptyList()
            )
        }
    }

    data class ManageServicesSection(
        val services: List<Service>,
    ) : ManageEmployeeServicesViewState() {
        companion object {
            val initial = ManageServicesSection(
                emptyList()
            )
        }
    }

}

internal abstract class ManageEmployeeServiceScreenViewModel :
    BaseViewModel<ManageEmployeeServicesViewState>() {
    abstract fun addEmployeeService(employeeService: EmployeeService)
    abstract fun deleteEmployeeService(employeeService: EmployeeService)
    abstract fun updateEmployeeService(employeeService: EmployeeService)
}

internal class ManageEmployeeServicesScreenViewModelImpl(
    private val getAllEmployeeServicesUseCase: GetAllEmployeeServicesUseCase,
    private val getAllEmployeesUseCase: GetAllEmployeesUseCase,
    private val getAllServicesUseCase: GetAllServicesUseCase,
    private val addEmployeeServiceToDatabaseUseCase: AddEmployeeSeviceToDatabaseUseCase,
    private val deleteEmployeeServiceFromDatabaseUseCase: DeleteEmployeeServiceFromDatabaseUseCase,
    private val updateEmployeeServiceToDatabaseUseCase: UpdateEmployeeServiceToDatabaseUseCase,
) : ManageEmployeeServiceScreenViewModel() {

    init {
        query {
            getAllEmployeeServicesUseCase()
                .map(ManageEmployeeServicesViewState::ManageEmployeeServiceSection)
        }

        query {
            getAllEmployeesUseCase()
                .map(ManageEmployeeServicesViewState::ManageEmployeesSection)
        }

        query {
            getAllServicesUseCase()
                .map(ManageEmployeeServicesViewState::ManageServicesSection)
        }
    }

    override fun addEmployeeService(employeeService: EmployeeService) {
        runCommand {
            addEmployeeServiceToDatabaseUseCase(employeeService)
        }

        query {
            getAllEmployeeServicesUseCase()
                .map(ManageEmployeeServicesViewState::ManageEmployeeServiceSection)
                .onStart { delay(50) }
        }
    }

    override fun deleteEmployeeService(employeeService: EmployeeService) {
        runCommand {
            deleteEmployeeServiceFromDatabaseUseCase(employeeService)
        }

        query {
            getAllEmployeeServicesUseCase()
                .map(ManageEmployeeServicesViewState::ManageEmployeeServiceSection)
                .onStart { delay(50) }
        }
    }

    override fun updateEmployeeService(employeeService: EmployeeService) {
        runCommand {
            updateEmployeeServiceToDatabaseUseCase(employeeService)
        }

        query {
            getAllEmployeeServicesUseCase()
                .map(ManageEmployeeServicesViewState::ManageEmployeeServiceSection)
                .onStart { delay(50) }
        }
    }

}
