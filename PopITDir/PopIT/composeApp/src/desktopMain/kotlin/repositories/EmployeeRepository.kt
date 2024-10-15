package repositories

import domain.model.EmployeeDetails
import domain.model.EmployeeService
import domain.model.WorkPosition
import kotlinx.coroutines.flow.Flow
import source.local.LocalDataSource

internal interface EmployeeRepository {



    fun getAllEmployees(): Flow<List<EmployeeDetails>>

    fun getAllEmployeesAndAdmins(): Flow<List<EmployeeDetails>>

    fun getAllEmployeeServices(): Flow<List<EmployeeService>>

    fun getAllWorkPositions(): Flow<List<WorkPosition>>

    fun getAdminAuth(email: String, password: String, isAdmin: Boolean): Flow<EmployeeDetails>

    fun getEmployeeAuth(email: String, password: String, isAdmin: Boolean): Flow<EmployeeDetails>

    fun getAdminDetails(employeeId: Int, isAdmin: Boolean): Flow<EmployeeDetails>

    fun getEmployeeDetails(employeeId: Int, isAdmin: Boolean): Flow<EmployeeDetails>

    suspend fun addEmployeeToDatabase(employee: EmployeeDetails)

    suspend fun deleteEmployeeFromDatabase(employee: EmployeeDetails)

    suspend fun updateEmployeeToDatabase(employee: EmployeeDetails)

    suspend fun addWorkPositionToDatabase(workPosition: WorkPosition)

    suspend fun deleteWorkPositionFromDatabase(positionId: Int)

    suspend fun updateWorkPositionToDatabase(workPosition: WorkPosition)

    suspend fun addEmployeeServiceToDatabase(employeeService: EmployeeService)

    suspend fun deleteEmployeeServiceFromDatabase(employeeService: EmployeeService)

    suspend fun updateEmployeeServiceToDatabase(employeeService: EmployeeService)


}

internal class EmployeeRepositoryImpl(
    private val localDataSource: LocalDataSource,
) : EmployeeRepository {

    override fun getAllEmployees(): Flow<List<EmployeeDetails>> {
        return localDataSource.getAllEmployees()
    }

    override fun getAllEmployeesAndAdmins(): Flow<List<EmployeeDetails>> {
        return localDataSource.getAllEmployeesAndAdmins()
    }

    override fun getAllEmployeeServices(): Flow<List<EmployeeService>> {
        return localDataSource.getAllEmployeeServices()
    }

    override fun getAllWorkPositions(): Flow<List<WorkPosition>> {
        return localDataSource.getAllWorkPositions()
    }

    override fun getAdminAuth(
        email: String,
        password: String,
        isAdmin: Boolean
    ): Flow<EmployeeDetails> {
        return localDataSource.getEmployeeAuth(email, password, isAdmin)
    }

    override fun getEmployeeAuth(
        email: String,
        password: String,
        isAdmin: Boolean
    ): Flow<EmployeeDetails> {
        return localDataSource.getEmployeeAuth(email, password, isAdmin)
    }

    override fun getAdminDetails(employeeId: Int, isAdmin: Boolean): Flow<EmployeeDetails>  {
        return localDataSource.getEmployeeDetails(employeeId, isAdmin)
    }

    override fun getEmployeeDetails(
        employeeId: Int,
        isAdmin: Boolean
    ): Flow<EmployeeDetails> {
        return localDataSource.getEmployeeDetails(employeeId, isAdmin)
    }

    override suspend fun addEmployeeToDatabase(employee: EmployeeDetails) {
        localDataSource.addEmployeeToDatabase(employee)
    }

    override suspend fun deleteEmployeeFromDatabase(employee: EmployeeDetails) {
        localDataSource.deleteEmployeeFromDatabase(employee)
    }

    override suspend fun updateEmployeeToDatabase(employee: EmployeeDetails) {
        localDataSource.updateEmployeeToDatabase(employee)
    }

    override suspend fun addWorkPositionToDatabase(workPosition: WorkPosition) {
        localDataSource.addWorkPositionToDatabase(workPosition)
    }

    override suspend fun deleteWorkPositionFromDatabase(positionId: Int) {
        localDataSource.deleteWorkPositionFromDatabase(positionId)
    }

    override suspend fun updateWorkPositionToDatabase(workPosition: WorkPosition) {
        localDataSource.updateWorkPositionToDatabase(workPosition)
    }

    override suspend fun addEmployeeServiceToDatabase(employeeService: EmployeeService) {
        localDataSource.addEmployeeServiceToDatabase(employeeService)
    }

    override suspend fun deleteEmployeeServiceFromDatabase(employeeService: EmployeeService) {
        localDataSource.deleteEmployeeServiceFromDatabase(employeeService)
    }

    override suspend fun updateEmployeeServiceToDatabase(employeeService: EmployeeService) {
        localDataSource.updateEmployeeServiceToDatabase(employeeService)
    }
}
