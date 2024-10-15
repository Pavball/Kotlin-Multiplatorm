package ui.viewmodels

internal sealed class MainScreenViewState {
    data object Initial : MainScreenViewState()
}

internal abstract class MainScreenViewModel : BaseViewModel<MainScreenViewState>() {
    abstract fun setEmployeeDetails(id: Int, employeeName: String, admin: Boolean)

    abstract fun retrieveEmployeeId(): Int?

    abstract fun retrieveEmployeeName(): String?

    abstract fun retrieveIsAdmin(): Boolean?
}

internal class MainScreenViewModelImpl(
    var employeeId: Int? = null,
    var employeeName: String? = null,
    var isAdmin: Boolean? = null
) : MainScreenViewModel() {

    override fun setEmployeeDetails(id: Int, employeeName: String, admin: Boolean) {
        this.employeeId = id
        this.employeeName = employeeName
        this.isAdmin = admin
    }

    override fun retrieveEmployeeId(): Int? {
        return employeeId
    }

    override fun retrieveEmployeeName(): String? {
        return employeeName
    }

    override fun retrieveIsAdmin(): Boolean? {
        return isAdmin
    }
}
