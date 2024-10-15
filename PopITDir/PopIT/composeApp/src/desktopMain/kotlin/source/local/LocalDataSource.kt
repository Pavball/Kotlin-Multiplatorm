package source.local

import domain.model.EmployeeDetails
import domain.model.EmployeeService
import domain.model.Order
import domain.model.OrderDetail
import domain.model.Product
import domain.model.ProductCategory
import domain.model.Service
import domain.model.ServiceRequest
import domain.model.ServiceType
import domain.model.UserDetails
import domain.model.WorkPosition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

internal interface LocalDataSource {

    fun getAllOrders(): Flow<List<Order>>

    fun getAllProducts(): Flow<List<Product>>

    fun getAllServices(): Flow<List<Service>>

    fun getAllServiceTypes(): Flow<List<ServiceType>>

    fun getAllServiceRequests(): Flow<List<ServiceRequest>>

    fun getAllProductCategories(): Flow<List<ProductCategory>>

    fun getAllEmployees(): Flow<List<EmployeeDetails>>

    fun getAllEmployeesAndAdmins(): Flow<List<EmployeeDetails>>

    fun getAllEmployeeServices(): Flow<List<EmployeeService>>

    fun getAllWorkPositions(): Flow<List<WorkPosition>>

    fun getSpecificOrderDetails(orderId: Int): Flow<Order>

    fun getSpecificProductDetails(productId: Int): Flow<Product>

    fun getSpecificServiceRequestDetails(requestId: Int): Flow<ServiceRequest>

    fun getEmployeeAuth(email: String, password: String, isAdmin: Boolean): Flow<EmployeeDetails>

    fun getEmployeeDetails(employeeId: Int, isAdmin: Boolean): Flow<EmployeeDetails>

    //ADD STUFF TO DATABASE
    fun sendMessageToServiceRequest(requestId: Int, updatedCommunication: String)

    fun addProductToDatabase(product: Product)

    fun deleteProductFromDatabase(productId: Int)

    fun updateProductToDatabase(product: Product)

    fun addProductCategoryToDatabase(productCategory: ProductCategory)

    fun deleteProductCategoryFromDatabase(categoryId: Int)

    fun updateProductCategoryToDatabase(productCategory: ProductCategory)

    fun addServiceToDatabase(service: Service)

    fun deleteServiceFromDatabase(serviceId: Int)

    fun updateServiceToDatabase(service: Service)

    fun addServiceTypeToDatabase(serviceType: ServiceType)

    fun deleteServiceTypeFromDatabase(serviceTypeId: Int)

    fun updateServiceTypeToDatabase(serviceType: ServiceType)

    fun addEmployeeToDatabase(employeeDetails: EmployeeDetails)

    fun deleteEmployeeFromDatabase(employee: EmployeeDetails)

    fun updateEmployeeToDatabase(employeeDetails: EmployeeDetails)

    fun addEmployeeServiceToDatabase(employeeService: EmployeeService)

    fun deleteEmployeeServiceFromDatabase(employeeService: EmployeeService)

    fun updateEmployeeServiceToDatabase(employeeService: EmployeeService)

    fun addWorkPositionToDatabase(workPosition: WorkPosition)

    fun deleteWorkPositionFromDatabase(positionId: Int)

    fun updateWorkPositionToDatabase(workPosition: WorkPosition)

    fun assignServiceToEmployee(requestId: Int, employeeId: Int)

    fun isEmployeeAuthorizedForService(employeeId: Int, serviceId: Int): Flow<Boolean>

    fun finishServiceRequest(requestId: Int, finishedCommunication: String)

    fun completeOrder(orderId: Int)
}

internal class LocalDataSourceImpl(
    private val dbConnection: Connection
) : LocalDataSource {

    override fun getAllOrders(): Flow<List<Order>> = flow {
        val orders = mutableListOf<Order>()
        val query = """
        SELECT 
            o.orderId, 
            o.orderDate, 
            o.orderStatus, 
            o.totalAmount, 
            o.userId 
        FROM 
            `order` o
    """.trimIndent()

        val statement = dbConnection.createStatement()
        val resultSet = statement.executeQuery(query)

        while (resultSet.next()) {
            val order = Order(
                orderId = resultSet.getInt("orderId"),
                userId = resultSet.getInt("userId"),
                orderDate = resultSet.getString("orderDate"),
                orderStatus = resultSet.getString("orderStatus"),
                totalAmount = resultSet.getDouble("totalAmount"),
                userDetails = getUserDetails(resultSet.getInt("userId")),
                orderDetails = getOrderDetails(resultSet.getInt("orderId"))
            )
            orders.add(order)
        }

        emit(orders)
    }

    override fun getAllProducts(): Flow<List<Product>> = flow {
        val products = mutableListOf<Product>()
        val query = """
        SELECT 
            p.productId, 
            p.productName, 
            p.productDesc, 
            p.productPrice, 
            p.productStock,
            p.productImage,
            p.createdByAdminId,
            pc.categoryId,
            pc.categoryName,
            pc.categoryDesc
        FROM 
            `product` p
        JOIN ProductCategory pc ON p.categoryId = pc.categoryId
    """.trimIndent()

        val statement = dbConnection.createStatement()
        val resultSet = statement.executeQuery(query)

        while (resultSet.next()) {

            val productCategory = ProductCategory(
                categoryId = resultSet.getInt("categoryId"),
                categoryName = resultSet.getString("categoryName"),
                categoryDesc = resultSet.getString("categoryDesc")
            )

            val product = Product(
                productId = resultSet.getInt("productId"),
                productName = resultSet.getString("productName"),
                productDesc = resultSet.getString("productDesc"),
                productPrice = resultSet.getDouble("productPrice"),
                productStock = resultSet.getInt("productStock"),
                productImage = resultSet.getString("productImage"),
                categoryId = resultSet.getInt("categoryId"),
                productCategory = productCategory,
                createdByAdminId = resultSet.getInt("createdByAdminId")
            )
            products.add(product)
        }

        emit(products)
    }

    override fun getAllProductCategories(): Flow<List<ProductCategory>> = flow {
        val productCategories = mutableListOf<ProductCategory>()
        val query = """
        SELECT 
            *
        FROM 
            `ProductCategory`
    """.trimIndent()

        val statement = dbConnection.createStatement()
        val resultSet = statement.executeQuery(query)

        while (resultSet.next()) {

            val productCategory = ProductCategory(
                categoryId = resultSet.getInt("categoryId"),
                categoryName = resultSet.getString("categoryName"),
                categoryDesc = resultSet.getString("categoryDesc")
            )

            productCategories.add(productCategory)
        }

        emit(productCategories)
    }

    override fun getAllServices(): Flow<List<Service>> {
        return flow {
            val connection = getConnection()
            val services = mutableListOf<Service>()

            val query = """
                SELECT s.*, st.typeId, st.typeName, st.typeDesc
                FROM service s
                LEFT JOIN serviceType st ON s.serviceTypeId = st.typeId
            """.trimIndent()

            val resultSet = connection.prepareStatement(query).executeQuery()

            while (resultSet.next()) {
                val serviceType = ServiceType(
                    typeId = resultSet.getInt("typeId"),
                    typeName = resultSet.getString("typeName"),
                    typeDesc = resultSet.getString("typeDesc")
                )

                val service = Service(
                    serviceId = resultSet.getInt("serviceId"),
                    serviceName = resultSet.getString("serviceName"),
                    serviceDesc = resultSet.getString("serviceDesc"),
                    servicePrice = resultSet.getDouble("servicePrice"),
                    serviceTypeId = resultSet.getInt("serviceTypeId"),
                    serviceType = serviceType,
                    createdByAdminId = resultSet.getInt("createdByAdminId")
                )
                services.add(service)
            }
            emit(services)
        }
    }

    override fun getAllServiceTypes(): Flow<List<ServiceType>> {
        return flow {
            val connection = getConnection()
            val serviceTypes = mutableListOf<ServiceType>()

            val query = """
                SELECT *
                FROM serviceType
            """.trimIndent()

            val resultSet = connection.prepareStatement(query).executeQuery()

            while (resultSet.next()) {
                val serviceType = ServiceType(
                    typeId = resultSet.getInt("typeId"),
                    typeName = resultSet.getString("typeName"),
                    typeDesc = resultSet.getString("typeDesc")
                )

                serviceTypes.add(serviceType)
            }
            emit(serviceTypes)
        }
    }

    override fun getAllEmployees(): Flow<List<EmployeeDetails>> {
        return flow {
            val connection = getConnection()
            val employees = mutableListOf<EmployeeDetails>()

            val query = """
    SELECT 
        e.employeeId as id,
        e.name,
        e.surname,
        e.phone as phoneNumber,
        e.email as email,
        e.password as password,
        e.workPositionId as workPositionId,
        false as isAdmin
    FROM employee e

""".trimIndent()


            val resultSet = connection.prepareStatement(query).executeQuery()

            while (resultSet.next()) {
                val employeeDetails = EmployeeDetails(
                    employeeId = resultSet.getInt("id"),
                    name = resultSet.getString("name"),
                    surname = resultSet.getString("surname"),
                    phoneNumber = resultSet.getString("phoneNumber"),
                    email = resultSet.getString("email"),
                    password = resultSet.getString("password"),
                    workPositionId = resultSet.getInt("workPositionId"),
                    workPosition = getWorkPositionForEmployee(resultSet.getInt("workPositionId")),
                    isAdmin = resultSet.getBoolean("isAdmin"),
                    roleChanged = false
                )
                employees.add(employeeDetails)
            }

            emit(employees)

        }
    }


    override fun getAllEmployeesAndAdmins(): Flow<List<EmployeeDetails>> {
        return flow {
            val connection = getConnection()
            val employees = mutableListOf<EmployeeDetails>()

            val query = """
    SELECT 
        e.employeeId as id,
        e.name,
        e.surname,
        e.phone as phoneNumber,
        e.email as email,
        e.password as password,
        e.workPositionId as workPositionId,
        false as isAdmin
    FROM employee e

    UNION

    SELECT 
        a.adminId as id,
        a.name,
        a.surname,
        a.phone as phoneNumber,
        a.email as email,
        a.password as password,
        NULL as workPositionId,
        true as isAdmin
    FROM admin a
""".trimIndent()


            val resultSet = connection.prepareStatement(query).executeQuery()

            while (resultSet.next()) {
                val employeeDetails = EmployeeDetails(
                    employeeId = resultSet.getInt("id"),
                    name = resultSet.getString("name"),
                    surname = resultSet.getString("surname"),
                    phoneNumber = resultSet.getString("phoneNumber"),
                    email = resultSet.getString("email"),
                    password = resultSet.getString("password"),
                    workPositionId = resultSet.getInt("workPositionId"),
                    workPosition = getWorkPositionForEmployee(resultSet.getInt("workPositionId")),
                    isAdmin = resultSet.getBoolean("isAdmin"),
                    roleChanged = false
                )
                employees.add(employeeDetails)
            }

            emit(employees)

        }
    }

    // The function to get all employee services
    override fun getAllEmployeeServices(): Flow<List<EmployeeService>> {
        return flow {
            val connection = getConnection()
            val employeeServices = mutableListOf<EmployeeService>()

            val query = """
            SELECT 
                es.employeeServiceId,
                es.serviceId,
                es.employeeId,
                e.name,
                e.surname,
                e.phone AS phoneNumber,
                false AS isAdmin,
                s.serviceName,
                s.serviceDesc
            FROM employeeservice es
            JOIN employee e ON es.employeeId = e.employeeId
            JOIN service s ON es.serviceId = s.serviceId  
        """.trimIndent()

            val resultSet = connection.prepareStatement(query).executeQuery()

            while (resultSet.next()) {
                val employeeDetails = EmployeeDetails(
                    employeeId = resultSet.getInt("employeeId"),
                    name = resultSet.getString("name"),
                    surname = resultSet.getString("surname"),
                    phoneNumber = resultSet.getString("phoneNumber"),
                    email = "",
                    password = "",
                    workPositionId = 0,
                    workPosition = WorkPosition(0, "", ""),
                    isAdmin = false,
                    roleChanged = false
                )

                val service = Service(
                    serviceId = resultSet.getInt("serviceId"),
                    serviceName = resultSet.getString("serviceName"),
                    serviceDesc = resultSet.getString("serviceDesc"),
                    servicePrice = 0.0,
                    serviceTypeId = 0,
                    serviceType = ServiceType(0, "", ""),
                    createdByAdminId = 0
                )

                val employeeService = EmployeeService(
                    employeeServiceId = resultSet.getInt("employeeServiceId"),
                    serviceId = resultSet.getInt("serviceId"),
                    employeeId = resultSet.getInt("employeeId"),
                    service = service,
                    employee = employeeDetails
                )

                employeeServices.add(employeeService)
            }

            emit(employeeServices)
        }
    }

    override fun getAllWorkPositions(): Flow<List<WorkPosition>> {
        return flow {
            val workPositions = mutableListOf<WorkPosition>()

            val query = """
                SELECT *
                FROM workposition
            """.trimIndent()

            val resultSet = dbConnection.prepareStatement(query).executeQuery()

            while (resultSet.next()) {
                val workPosition = WorkPosition(
                    positionId = resultSet.getInt("positionId"),
                    positionName = resultSet.getString("positionName"),
                    positionDesc = resultSet.getString("positionDesc")
                )

                workPositions.add(workPosition)
            }
            emit(workPositions)
        }
    }

    override fun getAllServiceRequests(): Flow<List<ServiceRequest>> {
        return flow {
            val connection = getConnection()
            val serviceRequests = mutableListOf<ServiceRequest>()

            val query = """
                SELECT sr.*, s.*, st.typeId, st.typeName, st.typeDesc
                FROM servicerequest sr
                LEFT JOIN service s ON sr.serviceId = s.serviceId
                LEFT JOIN serviceType st ON s.serviceTypeId = st.typeId
            """.trimIndent()

            val resultSet = connection.prepareStatement(query).executeQuery()

            while (resultSet.next()) {
                val serviceType = ServiceType(
                    typeId = resultSet.getInt("typeId"),
                    typeName = resultSet.getString("typeName"),
                    typeDesc = resultSet.getString("typeDesc")
                )

                val service = Service(
                    serviceId = resultSet.getInt("serviceId"),
                    serviceName = resultSet.getString("serviceName"),
                    serviceDesc = resultSet.getString("serviceDesc"),
                    servicePrice = resultSet.getDouble("servicePrice"),
                    serviceTypeId = resultSet.getInt("serviceTypeId"),
                    serviceType = serviceType,
                    createdByAdminId = resultSet.getInt("createdByAdminId")
                )

                val serviceRequest = ServiceRequest(
                    requestId = resultSet.getInt("requestId"),
                    requestDate = resultSet.getString("requestDate"),
                    requestStatus = resultSet.getString("requestStatus"),
                    problemDesc = resultSet.getString("problemDesc"),
                    userId = resultSet.getInt("userId"),
                    createdByAdminId = resultSet.getInt("createdByAdminId"),
                    serviceId = resultSet.getInt("serviceId"),
                    service = service,
                    employeeId = resultSet.getInt("employeeId"),
                    communication = resultSet.getString("communication"),
                    userDetails = getUserDetails(resultSet.getInt("userId"))
                )
                serviceRequests.add(serviceRequest)
            }
            emit(serviceRequests)
        }
    }

    override fun sendMessageToServiceRequest(requestId: Int, updatedCommunication: String) {

        val query = """UPDATE ServiceRequest 
            SET communication = ? 
            WHERE requestId = ?
            """.trimIndent()

        val preparedStatement = dbConnection.prepareStatement(query)

        try {
            preparedStatement.setString(1, updatedCommunication)
            preparedStatement.setInt(2, requestId)

            val rowsUpdated = preparedStatement.executeUpdate()

            if (rowsUpdated > 0) {
                println("Communication updated successfully for requestId: $requestId")
            } else {
                println("No service request found with requestId: $requestId")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun addProductToDatabase(product: Product) {

        val query = """
        INSERT INTO product (productName, productDesc, productPrice, productStock, productImage, categoryId, createdByAdminId) 
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """
        val preparedStatement = dbConnection.prepareStatement(query)

        try {
            // Postavite parametre za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setString(1, product.productName)
            preparedStatement.setString(2, product.productDesc)
            preparedStatement.setDouble(3, product.productPrice)
            preparedStatement.setInt(4, product.productStock)
            preparedStatement.setString(5, product.productImage)
            preparedStatement.setInt(6, product.categoryId)
            preparedStatement.setInt(7, product.createdByAdminId ?: java.sql.Types.NULL)

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("Product added successfully! Rows affected: $rowsAffected")

        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun deleteProductFromDatabase(productId: Int) {
        val connection = getConnection()

        val query = "DELETE FROM product WHERE productId = ?"

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavite parametar za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setInt(1, productId)

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("Product deleted successfully! Rows affected: $rowsAffected")

            // Zatvorite resurse
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun updateProductToDatabase(product: Product) {
        val connection = getConnection()

        val query =
            """UPDATE product SET 
                productName = ?, 
                productDesc = ?, 
                productPrice = ?, 
                productStock = ?, 
                productImage = ?,
                categoryId = ?,
                createdByAdminId = ?
                WHERE productId = ?""".trimIndent()

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavite parametre za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setString(1, product.productName)
            preparedStatement.setString(2, product.productDesc)
            preparedStatement.setDouble(3, product.productPrice)
            preparedStatement.setInt(4, product.productStock)
            preparedStatement.setString(5, product.productImage)
            preparedStatement.setInt(6, product.categoryId)
            product.createdByAdminId?.let { preparedStatement.setInt(7, it) }
            preparedStatement.setInt(8, product.productId)

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("Product updated successfully! Rows affected: $rowsAffected")

            // Zatvorite resurse
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun addProductCategoryToDatabase(productCategory: ProductCategory) {
        val connection = getConnection()

        val query = """
        INSERT INTO productCategory (categoryName, categoryDesc) 
        VALUES (?, ?)
    """
        val preparedStatement = connection.prepareStatement(query)

        try {
            // Set the parameters for the PreparedStatement
            preparedStatement.setString(1, productCategory.categoryName)
            preparedStatement.setString(2, productCategory.categoryDesc)
            // Execute the SQL query
            val rowsAffected = preparedStatement.executeUpdate()
            println("Product category added successfully! Rows affected: $rowsAffected")

            // Close resources
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun deleteProductCategoryFromDatabase(categoryId: Int) {
        val connection = getConnection()

        val query = "DELETE FROM productCategory WHERE categoryId = ?"

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Set the parameter for the PreparedStatement
            preparedStatement.setInt(1, categoryId)

            // Execute the SQL query
            val rowsAffected = preparedStatement.executeUpdate()
            println("Product category deleted successfully! Rows affected: $rowsAffected")

            // Close resources
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }


    override fun updateProductCategoryToDatabase(productCategory: ProductCategory) {
        val connection = getConnection()

        val query =
            """UPDATE productCategory SET 
                categoryName = ?,
                categoryDesc = ?
                WHERE categoryId = ?""".trimIndent()

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Set the parameters for the PreparedStatement
            preparedStatement.setString(1, productCategory.categoryName)
            preparedStatement.setString(2, productCategory.categoryDesc)
            preparedStatement.setInt(3, productCategory.categoryId)

            // Execute the SQL query
            val rowsAffected = preparedStatement.executeUpdate()
            println("Product category updated successfully! Rows affected: $rowsAffected")

            // Close resources
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }


    override fun addServiceToDatabase(service: Service) {
        val connection = getConnection()

        val query = """
        INSERT INTO service (serviceName, serviceDesc, servicePrice, serviceTypeId, createdByAdminId) 
        VALUES (?, ?, ?, ?, ?)
    """
        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavite parametre za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setString(1, service.serviceName)
            preparedStatement.setString(2, service.serviceDesc)
            preparedStatement.setDouble(3, service.servicePrice)
            service.serviceTypeId?.let { preparedStatement.setInt(4, it) }
            service.createdByAdminId?.let { preparedStatement.setInt(5, it) }

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("Service updated successfully! Rows affected: $rowsAffected")

            // Zatvorite resurse
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun deleteServiceFromDatabase(serviceId: Int) {
        val connection = getConnection()

        val query = "DELETE FROM service WHERE serviceId = ?"

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavite parametar za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setInt(1, serviceId)

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("Product deleted successfully! Rows affected: $rowsAffected")

            // Zatvorite resurse
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun updateServiceToDatabase(service: Service) {
        val connection = getConnection()

        val query =
            """UPDATE service SET 
                serviceName = ?, 
                serviceDesc = ?, 
                servicePrice = ?, 
                serviceTypeId = ?, 
                createdByAdminId = ?
                WHERE serviceId = ?""".trimIndent()

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavite parametre za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setString(1, service.serviceName)
            preparedStatement.setString(2, service.serviceDesc)
            preparedStatement.setDouble(3, service.servicePrice)
            service.serviceTypeId?.let { preparedStatement.setInt(4, it) }
            service.createdByAdminId?.let { preparedStatement.setInt(5, it) }
            preparedStatement.setInt(6, service.serviceId)

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("Service updated successfully! Rows affected: $rowsAffected")

            // Zatvorite resurse
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun addServiceTypeToDatabase(serviceType: ServiceType) {
        val connection = getConnection()

        val query = """
        INSERT INTO servicetype (typeName, typeDesc)
        VALUES (?, ?)
    """.trimIndent()

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavite parametre za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setString(1, serviceType.typeName)
            preparedStatement.setString(2, serviceType.typeDesc)

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("ServiceType added successfully! Rows affected: $rowsAffected")

            // Zatvorite resurse
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun deleteServiceTypeFromDatabase(serviceTypeId: Int) {
        val connection = getConnection()

        val query = """
        DELETE FROM servicetype 
        WHERE typeId = ?
    """.trimIndent()

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavite parametar za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setInt(1, serviceTypeId)

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("ServiceType deleted successfully! Rows affected: $rowsAffected")

            // Zatvorite resurse
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun updateServiceTypeToDatabase(serviceType: ServiceType) {
        val connection = getConnection()

        val query = """
        UPDATE servicetype SET 
            typeName = ?, 
            typeDesc = ?
        WHERE typeId = ?
    """.trimIndent()

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavite parametre za pripremljenu izjavu (PreparedStatement)
            preparedStatement.setString(1, serviceType.typeName)
            preparedStatement.setString(2, serviceType.typeDesc)
            preparedStatement.setInt(3, serviceType.typeId)

            // Izvršite SQL upit
            val rowsAffected = preparedStatement.executeUpdate()
            println("ServiceType updated successfully! Rows affected: $rowsAffected")

            // Zatvorite resurse
            preparedStatement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }


    override fun addEmployeeToDatabase(employeeDetails: EmployeeDetails) {
        val table = if (employeeDetails.isAdmin) "admin" else "employee"
        val query = """
        INSERT INTO $table (name, surname, phone, email, password${if (!employeeDetails.isAdmin) ", workPositionId" else ""})
        VALUES (?, ?, ?, ?, ?${if (!employeeDetails.isAdmin) ", ?" else ""})
    """.trimIndent()

        val preparedStatement =
            dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS).apply {
                setString(1, employeeDetails.name)
                setString(2, employeeDetails.surname)
                setString(3, employeeDetails.phoneNumber)
                setString(4, employeeDetails.email)
                setString(5, employeeDetails.password)
                if (!employeeDetails.isAdmin) {
                    setInt(
                        6,
                        employeeDetails.workPositionId
                            ?: throw IllegalArgumentException("WorkPositionId cannot be null for non-admin employees")
                    )
                }
            }

        preparedStatement.executeUpdate()

        // Dobijanje generiranog ID-a zaposlenika
        val generatedKeys = preparedStatement.generatedKeys
        if (generatedKeys.next() && !employeeDetails.isAdmin) {
            val employeeId = generatedKeys.getInt(1)
            val wpQuery = """
            INSERT INTO workposition_has_employee (positionId, employeeId, dateFrom)
            VALUES (?, ?, CURRENT_DATE)
        """.trimIndent()

            val wpStatement = dbConnection.prepareStatement(wpQuery).apply {
                setInt(
                    1,
                    employeeDetails.workPositionId
                        ?: throw IllegalArgumentException("WorkPositionId cannot be null")
                )
                setInt(2, employeeId)
            }

            wpStatement.executeUpdate()
        }
    }

    override fun deleteEmployeeFromDatabase(employee: EmployeeDetails) {

        if (!employee.isAdmin) {
            val queryEmployee = """
        DELETE FROM employee 
        WHERE employeeId = ?
    """.trimIndent()

            val preparedStatementEmployee = dbConnection.prepareStatement(queryEmployee)

            preparedStatementEmployee.setInt(1, employee.employeeId)

            preparedStatementEmployee.executeUpdate()
        } else {
            val queryAdmin = """
        DELETE FROM admin 
        WHERE adminId = ?
    """.trimIndent()

            val preparedStatementAdmin = dbConnection.prepareStatement(queryAdmin)

            preparedStatementAdmin.setInt(1, employee.employeeId)

            preparedStatementAdmin.executeUpdate()
        }

    }


    override fun updateEmployeeToDatabase(employeeDetails: EmployeeDetails) {
        val isBecomingAdmin = employeeDetails.roleChanged && employeeDetails.isAdmin
        val isBecomingEmployee = employeeDetails.roleChanged && !employeeDetails.isAdmin

        if (isBecomingAdmin) {
            // Delete from workposition_has_employee before deleting from employee
            val deleteWpQuery = """
        DELETE FROM workposition_has_employee 
        WHERE employeeId = ?
        """.trimIndent()

            val deleteWpStatement = dbConnection.prepareStatement(deleteWpQuery).apply {
                setInt(1, employeeDetails.employeeId)
            }
            deleteWpStatement.executeUpdate()

            // Delete from employee table
            val deleteEmployeeQuery = """
        DELETE FROM employee 
        WHERE employeeId = ?
        """.trimIndent()

            val deleteEmployeeStatement = dbConnection.prepareStatement(deleteEmployeeQuery).apply {
                setInt(1, employeeDetails.employeeId)
            }
            deleteEmployeeStatement.executeUpdate()

            // Insert into admin table
            val insertAdminQuery = """
        INSERT INTO admin (adminId, name, surname, phone, email, password) 
        VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent()

            val insertAdminStatement = dbConnection.prepareStatement(insertAdminQuery).apply {
                setInt(1, employeeDetails.employeeId)
                setString(2, employeeDetails.name)
                setString(3, employeeDetails.surname)
                setString(4, employeeDetails.phoneNumber)
                setString(5, employeeDetails.email)
                setString(6, employeeDetails.password)
            }
            insertAdminStatement.executeUpdate()

        } else if (isBecomingEmployee) {
            // Delete from admin table
            val deleteAdminQuery = """
        DELETE FROM admin 
        WHERE adminId = ?
        """.trimIndent()

            val deleteAdminStatement = dbConnection.prepareStatement(deleteAdminQuery).apply {
                setInt(1, employeeDetails.employeeId)
            }
            deleteAdminStatement.executeUpdate()

            // Insert into employee table
            val insertEmployeeQuery = """
        INSERT INTO employee (employeeId, name, surname, phone, email, password, workPositionId) 
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

            val insertEmployeeStatement = dbConnection.prepareStatement(insertEmployeeQuery).apply {
                setInt(1, employeeDetails.employeeId)
                setString(2, employeeDetails.name)
                setString(3, employeeDetails.surname)
                setString(4, employeeDetails.phoneNumber)
                setString(5, employeeDetails.email)
                setString(6, employeeDetails.password)
                setInt(
                    7,
                    employeeDetails.workPositionId
                        ?: throw IllegalArgumentException("WorkPositionId cannot be null for employees")
                )
            }
            insertEmployeeStatement.executeUpdate()

            // Update workposition_has_employee table
            val deleteWpQuery = """
        DELETE FROM workposition_has_employee 
        WHERE employeeId = ?
        """.trimIndent()

            val deleteWpStatement = dbConnection.prepareStatement(deleteWpQuery).apply {
                setInt(1, employeeDetails.employeeId)
            }
            deleteWpStatement.executeUpdate()

            val insertWpQuery = """
        INSERT INTO workposition_has_employee (positionId, employeeId, dateFrom)
        VALUES (?, ?, CURRENT_DATE)
        """.trimIndent()

            val insertWpStatement = dbConnection.prepareStatement(insertWpQuery).apply {
                setInt(
                    1,
                    employeeDetails.workPositionId
                        ?: throw IllegalArgumentException("WorkPositionId cannot be null")
                )
                setInt(2, employeeDetails.employeeId)
            }
            insertWpStatement.executeUpdate()

        } else {
            // Update employee/admin if role hasn't changed
            val table = if (employeeDetails.isAdmin) "admin" else "employee"
            val tableId = if (employeeDetails.isAdmin) "adminId" else "employeeId"

            val query = """
        UPDATE $table 
        SET name = ?, surname = ?, phone = ?, email = ?, password = ?${if (!employeeDetails.isAdmin) ", workPositionId = ?" else ""}
        WHERE $tableId = ?
        """.trimIndent()

            val preparedStatement = dbConnection.prepareStatement(query).apply {
                setString(1, employeeDetails.name)
                setString(2, employeeDetails.surname)
                setString(3, employeeDetails.phoneNumber)
                setString(4, employeeDetails.email)
                setString(5, employeeDetails.password)
                if (!employeeDetails.isAdmin) {
                    setInt(
                        6,
                        employeeDetails.workPositionId
                            ?: throw IllegalArgumentException("WorkPositionId cannot be null for non-admin employees")
                    )
                    setInt(7, employeeDetails.employeeId)
                } else {
                    setInt(6, employeeDetails.employeeId)
                }
            }
            preparedStatement.executeUpdate()

            // Update workposition_has_employee table if the user is an employee
            if (!employeeDetails.isAdmin) {
                val deleteWpQuery = """
            DELETE FROM workposition_has_employee 
            WHERE employeeId = ?
            """.trimIndent()

                val deleteWpStatement = dbConnection.prepareStatement(deleteWpQuery).apply {
                    setInt(1, employeeDetails.employeeId)
                }
                deleteWpStatement.executeUpdate()

                val insertWpQuery = """
            INSERT INTO workposition_has_employee (positionId, employeeId, dateFrom)
            VALUES (?, ?, CURRENT_DATE)
            """.trimIndent()

                val insertWpStatement = dbConnection.prepareStatement(insertWpQuery).apply {
                    setInt(
                        1,
                        employeeDetails.workPositionId
                            ?: throw IllegalArgumentException("WorkPositionId cannot be null")
                    )
                    setInt(2, employeeDetails.employeeId)
                }
                insertWpStatement.executeUpdate()
            }
        }
    }

    override fun addEmployeeServiceToDatabase(employeeService: EmployeeService) {
        // Query to check if the serviceId-employeeId pair already exists
        val checkQuery = """
        SELECT COUNT(*) FROM employeeservice 
        WHERE serviceId = ? AND employeeId = ?
    """.trimIndent()

        val checkStatement = dbConnection.prepareStatement(checkQuery).apply {
            setInt(1, employeeService.serviceId)
            setInt(2, employeeService.employeeId)
        }

        val resultSet = checkStatement.executeQuery()
        resultSet.next()

        // If count is 0, the combination does not exist, so we can insert it
        if (resultSet.getInt(1) == 0) {
            val insertQuery = """
            INSERT INTO employeeservice (serviceId, employeeId)
            VALUES (?, ?)
        """.trimIndent()

            val insertStatement = dbConnection.prepareStatement(insertQuery).apply {
                setInt(1, employeeService.serviceId)
                setInt(2, employeeService.employeeId)
            }

            insertStatement.executeUpdate()
        } else {
            println("This employee-service combination already exists.")
            // You can throw an exception or handle this case as needed
        }
    }

    override fun deleteEmployeeServiceFromDatabase(employeeService: EmployeeService) {
        val query = """
        DELETE FROM employeeservice
        WHERE employeeServiceId = ?
    """.trimIndent()

        val preparedStatement = dbConnection.prepareStatement(query).apply {
            setInt(1, employeeService.employeeServiceId)
        }

        preparedStatement.executeUpdate()
    }

    override fun updateEmployeeServiceToDatabase(employeeService: EmployeeService) {
        // Query to check if the serviceId-employeeId pair already exists for a different employeeServiceId
        val checkQuery = """
        SELECT COUNT(*) FROM employeeservice 
        WHERE serviceId = ? AND employeeId = ? AND employeeServiceId != ?
    """.trimIndent()

        val checkStatement = dbConnection.prepareStatement(checkQuery).apply {
            setInt(1, employeeService.serviceId)
            setInt(2, employeeService.employeeId)
            setInt(3, employeeService.employeeServiceId)
        }

        val resultSet = checkStatement.executeQuery()
        resultSet.next()

        // If count is 0, the combination does not exist for another record, so we can update it
        if (resultSet.getInt(1) == 0) {
            val updateQuery = """
            UPDATE employeeservice
            SET serviceId = ?, employeeId = ?
            WHERE employeeServiceId = ?
        """.trimIndent()

            val updateStatement = dbConnection.prepareStatement(updateQuery).apply {
                setInt(1, employeeService.serviceId)
                setInt(2, employeeService.employeeId)
                setInt(3, employeeService.employeeServiceId)
            }

            updateStatement.executeUpdate()
        } else {
            println("This employee-service combination already exists for another record.")
            // You can throw an exception or handle this case as needed
        }
    }



    // Funkcija za dodavanje nove pozicije
    override fun addWorkPositionToDatabase(workPosition: WorkPosition) {
        val connection = getConnection()

        val query = """INSERT INTO workposition (positionName, positionDesc) VALUES (?, ?)"""

        val preparedStatement = connection.prepareStatement(query)

        try {
            preparedStatement.setString(1, workPosition.positionName)
            preparedStatement.setString(2, workPosition.positionDesc)

            val rowsAffected = preparedStatement.executeUpdate()
            println("WorkPosition added successfully! Rows affected: $rowsAffected")

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            preparedStatement.close()
            connection.close()
        }
    }

    // Funkcija za brisanje pozicije prema ID-u
    override fun deleteWorkPositionFromDatabase(positionId: Int) {
        val connection = getConnection()

        val query = """DELETE FROM workposition WHERE positionId = ?"""

        val preparedStatement = connection.prepareStatement(query)

        try {
            preparedStatement.setInt(1, positionId)

            val rowsAffected = preparedStatement.executeUpdate()
            println("WorkPosition deleted successfully! Rows affected: $rowsAffected")

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            preparedStatement.close()
            connection.close()
        }
    }

    // Funkcija za ažuriranje postojeće pozicije
    override fun updateWorkPositionToDatabase(workPosition: WorkPosition) {
        val connection = getConnection()

        val query =
            """UPDATE workposition SET 
            positionName = ?, 
            positionDesc = ? 
            WHERE positionId = ?""".trimIndent()

        val preparedStatement = connection.prepareStatement(query)

        try {
            preparedStatement.setString(1, workPosition.positionName)
            preparedStatement.setString(2, workPosition.positionDesc)
            preparedStatement.setInt(3, workPosition.positionId)

            val rowsAffected = preparedStatement.executeUpdate()
            println("WorkPosition updated successfully! Rows affected: $rowsAffected")

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            preparedStatement.close()
            connection.close()
        }
    }

    override fun assignServiceToEmployee(requestId: Int, employeeId: Int) {
        val connection = getConnection()

        val query = "UPDATE servicerequest SET employeeId = ? WHERE requestId = ?"

        val preparedStatement = connection.prepareStatement(query)

        try {
            preparedStatement.setInt(1, employeeId)
            preparedStatement.setInt(2, requestId)

            val rowsAffected = preparedStatement.executeUpdate()
            println("ServiceRequest assigned to employee successfully! Rows affected: $rowsAffected")

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            preparedStatement.close()
            connection.close()
        }
    }

    override fun isEmployeeAuthorizedForService(employeeId: Int, serviceId: Int): Flow<Boolean> =
        flow {
            val connection = getConnection()
            val query =
                "SELECT COUNT(*) FROM employeeservice WHERE employeeId = ? AND serviceId = ?"

            val preparedStatement = connection.prepareStatement(query)
            return@flow try {
                preparedStatement?.apply {
                    setInt(1, employeeId)
                    setInt(2, serviceId)
                }

                val resultSet = preparedStatement.executeQuery()
                resultSet.next()

                // Check if the count is greater than 0
                val isAuthorized = (resultSet.getInt(1)) > 0

                emit(isAuthorized)
            } catch (e: SQLException) {
                e.printStackTrace()
                emit(false)
            } finally {
                // Close resources
                preparedStatement?.close()
                connection.close()
            }

        }

    override fun finishServiceRequest(requestId: Int, finishedCommunication: String) {
        val connection = getConnection()
        val query =
            "UPDATE servicerequest SET requestStatus = ?, communication = ? WHERE requestId = ?"
        val preparedStatement = connection.prepareStatement(query)

        try {
            preparedStatement.setString(1, "Completed")
            preparedStatement.setString(2, finishedCommunication)
            preparedStatement.setInt(3, requestId)

            val rowsAffected = preparedStatement.executeUpdate()
            println("ServiceRequest finished successfully! Rows affected: $rowsAffected")

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                preparedStatement?.close()
                connection.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }


    override fun completeOrder(orderId: Int) {
        val connection = getConnection()

        // SQL upit za ažuriranje statusa narudžbe
        val query = "UPDATE `order` SET orderStatus = 'Completed' WHERE orderId = ?"

        val preparedStatement = connection.prepareStatement(query)

        try {
            // Postavljanje vrijednosti za `orderId`
            preparedStatement.setInt(1, orderId)

            // Izvršavanje upita
            val rowsAffected = preparedStatement.executeUpdate()
            println("Order completed successfully! Rows affected: $rowsAffected")

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            // Zatvaranje resursa
            preparedStatement.close()
            connection.close()
        }
    }


    override fun getSpecificOrderDetails(orderId: Int): Flow<Order> = flow {
        val query = """
        SELECT 
            o.orderId, 
            o.orderDate, 
            o.orderStatus, 
            o.totalAmount, 
            o.userId
        FROM 
            `order` o
        WHERE 
            o.orderId = ?
    """.trimIndent()

        val preparedStatement = dbConnection.prepareStatement(query)
        preparedStatement.setInt(1, orderId)
        val resultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            val order = Order(
                orderId = resultSet.getInt("orderId"),
                userId = resultSet.getInt("userId"),
                orderDate = resultSet.getString("orderDate"),
                orderStatus = resultSet.getString("orderStatus"),
                totalAmount = resultSet.getDouble("totalAmount"),
                userDetails = getUserDetails(resultSet.getInt("userId")),
                orderDetails = getOrderDetails(resultSet.getInt("orderId"))
            )
            emit(order)
        } else {
            throw Exception("Order with id $orderId not found")
        }
    }

    override fun getSpecificProductDetails(productId: Int): Flow<Product> = flow {
        val query = """
        SELECT 
            p.productId, 
            p.productName, 
            p.productDesc, 
            p.productPrice, 
            p.productStock,
            p.productImage,
            p.createdByAdminId,
            pc.categoryId,
            pc.categoryName,
            pc.categoryDesc
        FROM 
            `product` p
        JOIN ProductCategory pc ON p.categoryId = pc.categoryId
        WHERE 
            p.productId = ?
    """.trimIndent()

        val preparedStatement = dbConnection.prepareStatement(query)
        preparedStatement.setInt(1, productId)
        val resultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            val productCategory = ProductCategory(
                categoryId = resultSet.getInt("categoryId"),
                categoryName = resultSet.getString("categoryName"),
                categoryDesc = resultSet.getString("categoryDesc")
            )

            val product = Product(
                productId = resultSet.getInt("productId"),
                productName = resultSet.getString("productName"),
                productDesc = resultSet.getString("productDesc"),
                productPrice = resultSet.getDouble("productPrice"),
                productStock = resultSet.getInt("productStock"),
                productImage = resultSet.getString("productImage"),
                categoryId = resultSet.getInt("categoryId"),
                productCategory = productCategory,
                createdByAdminId = resultSet.getInt("createdByAdminId")
            )
            emit(product)
        } else {
            throw Exception("Order with id $productId not found")
        }
    }

    override fun getSpecificServiceRequestDetails(requestId: Int): Flow<ServiceRequest> = flow {
        val query = """
        SELECT sr.*, s.*, st.typeId, st.typeName, st.typeDesc
        FROM ServiceRequest sr
                LEFT JOIN service s ON sr.serviceId = s.serviceId
                LEFT JOIN serviceType st ON s.serviceTypeId = st.typeId
        WHERE sr.requestId = ?
    """.trimIndent()

        val preparedStatement = dbConnection.prepareStatement(query)
        preparedStatement.setInt(1, requestId)
        val resultSet = preparedStatement.executeQuery()



        if (resultSet.next()) {
            val serviceType = ServiceType(
                typeId = resultSet.getInt("typeId"),
                typeName = resultSet.getString("typeName"),
                typeDesc = resultSet.getString("typeDesc")
            )

            val service = Service(
                serviceId = resultSet.getInt("serviceId"),
                serviceName = resultSet.getString("serviceName"),
                serviceDesc = resultSet.getString("serviceDesc"),
                servicePrice = resultSet.getDouble("servicePrice"),
                serviceTypeId = resultSet.getInt("serviceTypeId"),
                serviceType = serviceType,
                createdByAdminId = resultSet.getInt("createdByAdminId")
            )

            val serviceRequest = ServiceRequest(
                requestId = resultSet.getInt("requestId"),
                requestDate = resultSet.getString("requestDate"),
                requestStatus = resultSet.getString("requestStatus"),
                problemDesc = resultSet.getString("problemDesc"),
                userId = resultSet.getInt("userId"),
                createdByAdminId = resultSet.getInt("createdByAdminId"),
                serviceId = resultSet.getInt("serviceId"),
                service = service,
                employeeId = resultSet.getInt("employeeId"),
                communication = resultSet.getString("communication"),
                userDetails = getUserDetails(resultSet.getInt("userId"))
            )

            emit(serviceRequest)

        } else {
            throw Exception("Order with id $requestId not found")
        }
    }

    override fun getEmployeeAuth(
        email: String,
        password: String,
        isAdmin: Boolean
    ): Flow<EmployeeDetails> = flow {
        val table = if (isAdmin) "admin" else "employee"
        val tableId = if (isAdmin) "adminId" else "employeeId"
        val query = """
        SELECT $tableId, name, surname, phone, email, password
        FROM $table 
        WHERE email = ? AND password = ?
    """.trimIndent()

        val preparedStatement = dbConnection.prepareStatement(query)
        preparedStatement.setString(1, email)
        preparedStatement.setString(2, password)

        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            val employeeId = resultSet.getInt(tableId)
            val workPosition = if (!isAdmin) getWorkPositionForEmployee(employeeId) else null

            emit(
                EmployeeDetails(
                    employeeId = employeeId,
                    name = resultSet.getString("name"),
                    surname = resultSet.getString("surname"),
                    phoneNumber = resultSet.getString("phone"),
                    email = resultSet.getString("email"),
                    password = resultSet.getString("password"),
                    workPositionId = workPosition?.positionId,
                    workPosition = workPosition,
                    isAdmin = isAdmin,
                    roleChanged = false
                )
            )
        }
    }


    override fun getEmployeeDetails(employeeId: Int, isAdmin: Boolean): Flow<EmployeeDetails> =
        flow {
            val table = if (isAdmin) "admin" else "employee"
            val tableId = if (isAdmin) "adminId" else "employeeId"
            val query = """
        SELECT $tableId, name, surname, phone, email, password
        FROM $table 
        WHERE $tableId = ?
    """.trimIndent()

            val preparedStatement = dbConnection.prepareStatement(query)
            preparedStatement.setInt(1, employeeId)

            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                val workPosition = if (!isAdmin) getWorkPositionForEmployee(employeeId) else null

                emit(
                    EmployeeDetails(
                        employeeId = resultSet.getInt(tableId),
                        name = resultSet.getString("name"),
                        surname = resultSet.getString("surname"),
                        phoneNumber = resultSet.getString("phone"),
                        email = resultSet.getString("email"),
                        password = resultSet.getString("password"),
                        workPositionId = workPosition?.positionId,
                        workPosition = workPosition,
                        isAdmin = isAdmin,
                        roleChanged = false
                    )
                )
            }
        }


    private fun getOrderDetails(orderId: Int): List<OrderDetail> {
        val orderDetails = mutableListOf<OrderDetail>()
        val query = """
            SELECT od.orderQuantity, od.unitPrice, p.productName
            FROM OrderDetail od
            JOIN Product p ON od.productId = p.productId
            WHERE od.orderId = ?
        """.trimIndent()

        val preparedStatement = dbConnection.prepareStatement(query)
        preparedStatement.setInt(1, orderId)
        val resultSet = preparedStatement.executeQuery()

        while (resultSet.next()) {
            val orderDetail = OrderDetail(
                productName = resultSet.getString("productName"),
                orderQuantity = resultSet.getInt("orderQuantity"),
                unitPrice = resultSet.getDouble("unitPrice")
            )
            orderDetails.add(orderDetail)
        }

        return orderDetails
    }

    private fun getUserDetails(userId: Int): UserDetails {
        val query = """
        SELECT 
            u.name, 
            u.surname, 
            u.phoneNumber, 
            a.street_no, 
            l.city, 
            l.postalCode
        FROM 
            `user` u
            JOIN Address a ON u.userId = a.userId
            JOIN Location l ON a.locationId = l.locationId
        WHERE 
            u.userId = ?
    """.trimIndent()

        dbConnection.prepareStatement(query).use { preparedStatement ->
            preparedStatement.setInt(1, userId)
            preparedStatement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    return UserDetails(
                        name = resultSet.getString("name"),
                        surname = resultSet.getString("surname"),
                        phoneNumber = resultSet.getString("phoneNumber"),
                        streetNo = resultSet.getString("street_no"),
                        city = resultSet.getString("city"),
                        postalCode = resultSet.getString("postalCode")
                    )
                }
            }
        }

        return UserDetails("Null", "Null", "Null", "Null", "Null", "Null")
    }

    private fun getWorkPositionForEmployee(employeeId: Int): WorkPosition? {
        val query = """
        SELECT 
            wp.positionId,
            wp.positionName,
            wp.positionDesc
        FROM 
            workposition wp
            JOIN workposition_has_employee wpe ON wp.positionId = wpe.positionId
            JOIN employee e ON e.employeeId = wpe.employeeId
        WHERE 
            e.employeeId = ?
            AND (wpe.dateUntil IS NULL OR wpe.dateUntil > CURDATE())
    """.trimIndent()

        dbConnection.prepareStatement(query).use { preparedStatement ->
            preparedStatement.setInt(1, employeeId)
            preparedStatement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    return WorkPosition(
                        positionId = resultSet.getInt("positionId"),
                        positionName = resultSet.getString("positionName"),
                        positionDesc = resultSet.getString("positionDesc")
                    )
                }
            }
        }

        return null
    }

}
