package ui.viewmodels

import domain.model.EmployeeDetails
import domain.model.WorkPosition
import domain.useCase.AddEmployeeToDatabaseUseCase
import domain.useCase.AddWorkPositionToDatabaseUseCase
import domain.useCase.DeleteEmployeeFromDatabaseUseCase
import domain.useCase.DeleteWorkPositionFromDatabaseUseCase
import domain.useCase.GetAllEmployeesAndAdminsUseCase
import domain.useCase.GetAllWorkPositionsUseCase
import domain.useCase.UpdateEmployeeToDatabaseUseCase
import domain.useCase.UpdateWorkPositionToDatabaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal sealed class ManageEmployeesViewState {

    data class ManageEmployeesSection(
        val manageEmployees: List<EmployeeDetails>,
    ) : ManageEmployeesViewState() {
        companion object {
            val initial = ManageEmployeesSection(
                emptyList()
            )
        }
    }

    data class ManageWorkPositionsSection(
        val manageWorkPositions: List<WorkPosition>,
    ) : ManageEmployeesViewState() {
        companion object {
            val initial = ManageWorkPositionsSection(
                emptyList()
            )
        }
    }

    data class ManageEmployeeServiceSection(
        val manageWorkPositions: List<WorkPosition>,
    ) : ManageEmployeesViewState() {
        companion object {
            val initial = ManageWorkPositionsSection(
                emptyList()
            )
        }
    }
}

internal abstract class ManageEmployeesScreenViewModel : BaseViewModel<ManageEmployeesViewState>() {
    abstract fun addEmployee(employee: EmployeeDetails)
    abstract fun deleteEmployee(employee: EmployeeDetails)
    abstract fun updateEmployee(employee: EmployeeDetails)
    abstract fun addWorkPosition(workPosition: WorkPosition)
    abstract fun deleteWorkPosition(positionId: Int)
    abstract fun updateWorkPosition(workPosition: WorkPosition)
}

internal class ManageEmployeesScreenViewModelImpl(
    private val getAllEmployeesAndAdminsUseCase: GetAllEmployeesAndAdminsUseCase,
    private val getAllWorkPositionsUseCase: GetAllWorkPositionsUseCase,
    private val addEmployeeToDatabaseUseCase: AddEmployeeToDatabaseUseCase,
    private val deleteEmployeeFromDatabaseUseCase: DeleteEmployeeFromDatabaseUseCase,
    private val updateEmployeeToDatabaseUseCase: UpdateEmployeeToDatabaseUseCase,
    private val addWorkPositionToDatabaseUseCase: AddWorkPositionToDatabaseUseCase,
    private val deleteWorkPositionFromDatabaseUseCase: DeleteWorkPositionFromDatabaseUseCase,
    private val updateWorkPositionToDatabaseUseCase: UpdateWorkPositionToDatabaseUseCase
) : ManageEmployeesScreenViewModel() {

    init {
        query {
            getAllEmployeesAndAdminsUseCase()
                .map(ManageEmployeesViewState::ManageEmployeesSection)
        }

        query {
            getAllWorkPositionsUseCase()
                .map(ManageEmployeesViewState::ManageWorkPositionsSection)
        }
    }

    override fun addEmployee(employee: EmployeeDetails) {
        runCommand {
            addEmployeeToDatabaseUseCase(employee)
        }

        query {
            getAllEmployeesAndAdminsUseCase()
                .map(ManageEmployeesViewState::ManageEmployeesSection)
                .onStart { delay(50) }
        }
    }

    override fun deleteEmployee(employee: EmployeeDetails) {
        runCommand {
            deleteEmployeeFromDatabaseUseCase(employee)
        }

        query {
            getAllEmployeesAndAdminsUseCase()
                .map(ManageEmployeesViewState::ManageEmployeesSection)
                .onStart { delay(50) }
        }
    }

    override fun updateEmployee(employee: EmployeeDetails) {
        runCommand {
            updateEmployeeToDatabaseUseCase(employee)
        }

        query {
            getAllEmployeesAndAdminsUseCase()
                .map(ManageEmployeesViewState::ManageEmployeesSection)
                .onStart { delay(50) }
        }
    }

    override fun addWorkPosition(workPosition: WorkPosition) {
        runCommand {
            addWorkPositionToDatabaseUseCase(workPosition)
        }

        query {
            getAllWorkPositionsUseCase()
                .map(ManageEmployeesViewState::ManageWorkPositionsSection)
                .onStart { delay(50) }
        }
    }

    override fun deleteWorkPosition(positionId: Int) {
        runCommand {
            deleteWorkPositionFromDatabaseUseCase(positionId)
        }

        query {
            getAllWorkPositionsUseCase()
                .map(ManageEmployeesViewState::ManageWorkPositionsSection)
                .onStart { delay(50) }
        }
    }

    override fun updateWorkPosition(workPosition: WorkPosition) {
        runCommand {
            updateWorkPositionToDatabaseUseCase(workPosition)
        }

        query {
            getAllWorkPositionsUseCase()
                .map(ManageEmployeesViewState::ManageWorkPositionsSection)
                .onStart { delay(50) }
        }
    }
}
