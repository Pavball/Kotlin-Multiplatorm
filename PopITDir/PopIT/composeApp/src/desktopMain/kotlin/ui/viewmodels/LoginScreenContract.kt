package ui.viewmodels

import domain.model.EmployeeDetails
import domain.model.WorkPosition
import domain.useCase.GetEmployeeAuthUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

internal sealed class LoginScreenViewState {

    data class LoginSection(
        val employeeDetail: EmployeeDetails,
    ) : LoginScreenViewState() {
        companion object {
            val initial = LoginSection(
                EmployeeDetails(
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    WorkPosition(0, "", ""),
                    isAdmin = false,
                    roleChanged = false
                )
            )
        }
    }
}

internal abstract class LoginScreenViewModel : BaseViewModel<LoginScreenViewState>() {
    abstract fun authenticateUser(email: String, password: String, isAdmin: Boolean)
}

internal class LoginScreenViewModelImpl(
    private val getEmployeeAuthUseCase: GetEmployeeAuthUseCase
) : LoginScreenViewModel() {

    override fun authenticateUser(email: String, password: String, isAdmin: Boolean) {
        query {
            getEmployeeAuthUseCase(email, password, isAdmin)
                .map(LoginScreenViewState::LoginSection)
        }
    }
}
