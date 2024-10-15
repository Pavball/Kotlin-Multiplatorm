package di

import domain.useCase.AddEmployeeService
import domain.useCase.AddEmployeeSeviceToDatabaseUseCase
import domain.useCase.AddEmployeeToDatabase
import domain.useCase.AddEmployeeToDatabaseUseCase
import domain.useCase.AddProductCategoryToDatabase
import domain.useCase.AddProductCategoryToDatabaseUseCase
import domain.useCase.AddProductToDatabase
import domain.useCase.AddProductToDatabaseUseCase
import domain.useCase.AddServiceToDatabase
import domain.useCase.AddServiceToDatabaseUseCase
import domain.useCase.AddServiceTypeToDatabase
import domain.useCase.AddServiceTypeToDatabaseUseCase
import domain.useCase.AddWorkPositionToDatabase
import domain.useCase.AddWorkPositionToDatabaseUseCase
import domain.useCase.AssignServiceToEmployee
import domain.useCase.AssignServiceToEmployeeUseCase
import domain.useCase.CheckEmployeeServiceAuthorization
import domain.useCase.CheckEmployeeServiceAuthorizationUseCase
import domain.useCase.CompleteOrder
import domain.useCase.CompleteOrderUseCase
import domain.useCase.DeleteEmployeeFromDatabase
import domain.useCase.DeleteEmployeeFromDatabaseUseCase
import domain.useCase.DeleteEmployeeService
import domain.useCase.DeleteEmployeeServiceFromDatabaseUseCase
import domain.useCase.DeleteProductCategoryFromDatabase
import domain.useCase.DeleteProductCategoryFromDatabaseUseCase
import domain.useCase.DeleteProductFromDatabase
import domain.useCase.DeleteProductFromDatabaseUseCase
import domain.useCase.DeleteServiceFromDatabase
import domain.useCase.DeleteServiceFromDatabaseUseCase
import domain.useCase.DeleteServiceTypeFromDatabase
import domain.useCase.DeleteServiceTypeFromDatabaseUseCase
import domain.useCase.DeleteWorkPositionFromDatabase
import domain.useCase.DeleteWorkPositionFromDatabaseUseCase
import domain.useCase.FinishServiceRequest
import domain.useCase.FinishServiceRequestUseCase
import domain.useCase.GetAllEmployeeServices
import domain.useCase.GetAllEmployeeServicesUseCase
import domain.useCase.GetAllEmployees
import domain.useCase.GetAllEmployeesAndAdmins
import domain.useCase.GetAllEmployeesAndAdminsUseCase
import domain.useCase.GetAllEmployeesUseCase
import domain.useCase.GetAllProductCategoryUseCase
import domain.useCase.GetAllProducts
import domain.useCase.GetAllProductsUseCase
import domain.useCase.GetAllServiceRequests
import domain.useCase.GetAllServiceRequestsUseCase
import domain.useCase.GetAllServiceTypes
import domain.useCase.GetAllServiceTypesUseCase
import domain.useCase.GetAllServices
import domain.useCase.GetAllServicesUseCase
import domain.useCase.GetAllWorkPositions
import domain.useCase.GetAllWorkPositionsUseCase
import domain.useCase.GetEmployeeAuth
import domain.useCase.GetEmployeeAuthUseCase
import domain.useCase.GetEmployeeDetails
import domain.useCase.GetEmployeeDetailsUseCase
import domain.useCase.GetOrderDetails
import domain.useCase.GetOrderDetailsUseCase
import domain.useCase.GetOrders
import domain.useCase.GetOrdersUseCase
import domain.useCase.GetProductCategory
import domain.useCase.GetProductDetails
import domain.useCase.GetProductDetailsUseCase
import domain.useCase.GetServiceRequestDetails
import domain.useCase.GetServiceRequestDetailsUseCase
import domain.useCase.SendMessageToServiceRequest
import domain.useCase.SendMessageToServiceRequestUseCase
import domain.useCase.UpdateEmployeeService
import domain.useCase.UpdateEmployeeServiceToDatabaseUseCase
import domain.useCase.UpdateEmployeeToDatabase
import domain.useCase.UpdateEmployeeToDatabaseUseCase
import domain.useCase.UpdateProductCategoryToDatabase
import domain.useCase.UpdateProductCategoryToDatabaseUseCase
import domain.useCase.UpdateProductToDatabase
import domain.useCase.UpdateProductToDatabaseUseCase
import domain.useCase.UpdateServiceToDatabase
import domain.useCase.UpdateServiceToDatabaseUseCase
import domain.useCase.UpdateServiceTypeToDatabase
import domain.useCase.UpdateServiceTypeToDatabaseUseCase
import domain.useCase.UpdateWorkPositionToDatabase
import domain.useCase.UpdateWorkPositionToDatabaseUseCase
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import repositories.EmployeeRepository
import repositories.EmployeeRepositoryImpl
import repositories.OrderRepository
import repositories.OrderRepositoryImpl
import repositories.ProductRepository
import repositories.ProductRepositoryImpl
import repositories.ServiceRepository
import repositories.ServiceRepositoryImpl
import source.local.LocalDataSource
import source.local.LocalDataSourceImpl
import source.local.getConnection
import ui.viewmodels.HomeScreenViewModel
import ui.viewmodels.HomeScreenViewModelImpl
import ui.viewmodels.LoginScreenViewModel
import ui.viewmodels.LoginScreenViewModelImpl
import ui.viewmodels.MainScreenViewModel
import ui.viewmodels.MainScreenViewModelImpl
import ui.viewmodels.ManageEmployeeServiceScreenViewModel
import ui.viewmodels.ManageEmployeeServicesScreenViewModelImpl
import ui.viewmodels.ManageEmployeesScreenViewModel
import ui.viewmodels.ManageEmployeesScreenViewModelImpl
import ui.viewmodels.ManageProductsScreenViewModel
import ui.viewmodels.ManageProductsScreenViewModelImpl
import ui.viewmodels.ManageServicesScreenViewModel
import ui.viewmodels.ManageServicesScreenViewModelImpl
import ui.viewmodels.OrderDetailsScreenViewModel
import ui.viewmodels.OrderDetailsScreenViewModelImpl
import ui.viewmodels.OrdersScreenViewModel
import ui.viewmodels.OrdersScreenViewModelImpl
import ui.viewmodels.ProductDetailsScreenViewModel
import ui.viewmodels.ProductDetailsScreenViewModelImpl
import ui.viewmodels.ProductsScreenViewModel
import ui.viewmodels.ProductsScreenViewModelImpl
import ui.viewmodels.ServiceRequestDetailsScreenViewModel
import ui.viewmodels.ServiceRequestDetailsScreenViewModelImpl
import ui.viewmodels.ServicesScreenViewModel
import ui.viewmodels.ServicesScreenViewModelImpl
import java.sql.Connection

internal val sharedModule = module {

    single<Connection> { getConnection() }

    single<LocalDataSource> { LocalDataSourceImpl(dbConnection = get()) }

    single<OrderRepository> { OrderRepositoryImpl(localDataSource = get()) }

    single<EmployeeRepository> { EmployeeRepositoryImpl(localDataSource = get()) }

    single<ServiceRepository> { ServiceRepositoryImpl(localDataSource = get()) }

    single<ProductRepository> { ProductRepositoryImpl(localDataSource = get()) }

    viewModel<MainScreenViewModel> { (employeeId: Int, employeeName: String, isAdmin: Boolean) ->
        MainScreenViewModelImpl(
            employeeId = employeeId,
            employeeName = employeeName,
            isAdmin = isAdmin
        )
    }

    viewModel<OrdersScreenViewModel> {
        OrdersScreenViewModelImpl(
            getOrdersUseCase = get()
        )
    }

    viewModel<OrderDetailsScreenViewModel> { (orderId: Int) ->
        OrderDetailsScreenViewModelImpl(
            orderId = orderId,
            getOrderDetailsUseCase = get(),
            completeOrderUseCase = get()
        )
    }

    viewModel<ProductsScreenViewModel> {
        ProductsScreenViewModelImpl(
            getProductsUseCase = get(),
            getProductCategoriesUseCase = get()
        )
    }

    viewModel<ProductDetailsScreenViewModel> { (productId: Int) ->
        ProductDetailsScreenViewModelImpl(
            productId = productId,
            getProductDetailsUseCase = get()
        )
    }

    viewModel<ServicesScreenViewModel> {
        ServicesScreenViewModelImpl(
            getAllServiceRequestsUseCase = get(),
            getAllServicesUseCase = get(),
            getAllEmployees = get()
        )
    }

    viewModel<ServiceRequestDetailsScreenViewModel> { (requestId: Int, employeeId: Int, serviceId: Int) ->
        ServiceRequestDetailsScreenViewModelImpl(
            requestId = requestId,
            employeeId = employeeId,
            serviceId = serviceId,
            getServiceRequestDetailsUseCase = get(),
            sendMessageToServiceRequestUseCase = get(),
            checkEmployeeServiceAuthorizationUseCase = get(),
            assignServiceToEmployeeUseCase = get(),
            finishServiceRequestUseCase = get()
        )
    }

    viewModel<LoginScreenViewModel> {
        LoginScreenViewModelImpl(
            getEmployeeAuthUseCase = get()
        )
    }

    viewModel<HomeScreenViewModel> { (employeeId: Int, isAdmin: Boolean) ->
        HomeScreenViewModelImpl(
            employeeId = employeeId,
            isAdmin = isAdmin,
            getEmployeeDetailsUseCase = get(),
            getOrdersUseCase = get(),
            getAllServiceRequestsUseCase = get()
        )
    }

    viewModel<ManageEmployeesScreenViewModel> {
        ManageEmployeesScreenViewModelImpl(
            getAllEmployeesAndAdminsUseCase = get(),
            getAllWorkPositionsUseCase = get(),
            addEmployeeToDatabaseUseCase = get(),
            deleteEmployeeFromDatabaseUseCase = get(),
            updateEmployeeToDatabaseUseCase = get(),
            addWorkPositionToDatabaseUseCase = get(),
            deleteWorkPositionFromDatabaseUseCase = get(),
            updateWorkPositionToDatabaseUseCase = get()
        )
    }

    viewModel<ManageServicesScreenViewModel> {
        ManageServicesScreenViewModelImpl(
            getAllServicesUseCase = get(),
            getAllServiceTypesUseCase = get(),
            addServiceToDatabaseUseCase = get(),
            deleteServiceFromDatabaseUseCase = get(),
            updateServiceToDatabaseUseCase = get(),
            addServiceTypeToDatabaseUseCase = get(),
            deleteServiceTypeFromDatabaseUseCase = get(),
            updateServiceTypeToDatabaseUseCase = get()

        )
    }

    viewModel<ManageProductsScreenViewModel> {
        ManageProductsScreenViewModelImpl(
            getAllProductsUseCase = get(),
            getAllProductCategoryUseCase = get(),
            addProductToDatabaseUseCase = get(),
            deleteProductFromDatabaseUseCase = get(),
            updateProductToDatabaseUseCase = get(),
            addProductCategoryToDatabaseUseCase = get(),
            deleteProductCategoryFromDatabaseUseCase = get(),
            updateProductCategoryToDatabaseUseCase = get()
        )
    }

    viewModel<ManageEmployeeServiceScreenViewModel> {
        ManageEmployeeServicesScreenViewModelImpl(
            getAllEmployeeServicesUseCase = get(),
            getAllEmployeesUseCase = get(),
            getAllServicesUseCase = get(),
            addEmployeeServiceToDatabaseUseCase = get(),
            deleteEmployeeServiceFromDatabaseUseCase = get(),
            updateEmployeeServiceToDatabaseUseCase = get()
        )
    }

    single<GetOrdersUseCase> {
        GetOrders(
            orderRepository = get()
        )
    }

    single<CompleteOrderUseCase> {
        CompleteOrder(
            orderRepository = get()
        )
    }


    single<GetOrderDetailsUseCase> {
        GetOrderDetails(
            orderRepository = get()
        )
    }

    single<GetAllProductsUseCase> {
        GetAllProducts(
            productRepository = get()
        )
    }

    single<GetProductDetailsUseCase> {
        GetProductDetails(
            productRepository = get()
        )
    }

    single<GetEmployeeAuthUseCase> {
        GetEmployeeAuth(
            employeeRepository = get()
        )
    }

    single<GetEmployeeDetailsUseCase> {
        GetEmployeeDetails(
            employeeRepository = get()
        )
    }

    single<GetAllServiceRequestsUseCase> {
        GetAllServiceRequests(
            serviceRepository = get()
        )
    }

    single<GetAllEmployeesUseCase> {
        GetAllEmployees(
            employeeRepository = get()
        )
    }

    single<GetAllEmployeesAndAdminsUseCase> {
        GetAllEmployeesAndAdmins(
            employeeRepository = get()
        )
    }

    single<GetAllEmployeeServicesUseCase> {
        GetAllEmployeeServices(
            employeeRepository = get()
        )
    }

    single<GetAllWorkPositionsUseCase> {
        GetAllWorkPositions(
            employeeRepository = get()
        )
    }

    single<GetServiceRequestDetailsUseCase> {
        GetServiceRequestDetails(
            serviceRepository = get()
        )
    }

    single<GetAllServicesUseCase> {
        GetAllServices(
            serviceRepository = get()
        )
    }

    single<GetAllServiceTypesUseCase> {
        GetAllServiceTypes(
            serviceRepository = get()
        )
    }

    single<GetAllProductCategoryUseCase> {
        GetProductCategory(
            productRepository = get()
        )
    }

    single<SendMessageToServiceRequestUseCase> {
        SendMessageToServiceRequest(
            serviceRepository = get()
        )
    }

    single<AddProductToDatabaseUseCase> {
        AddProductToDatabase(
            productRepository = get()
        )
    }

    single<DeleteProductFromDatabaseUseCase> {
        DeleteProductFromDatabase(
            productRepository = get()
        )
    }

    single<UpdateProductToDatabaseUseCase> {
        UpdateProductToDatabase(
            productRepository = get()
        )
    }

    single<AddProductCategoryToDatabaseUseCase> {
        AddProductCategoryToDatabase(
            productRepository = get()
        )
    }

    single<DeleteProductCategoryFromDatabaseUseCase> {
        DeleteProductCategoryFromDatabase(
            productRepository = get()
        )
    }

    single<UpdateProductCategoryToDatabaseUseCase> {
        UpdateProductCategoryToDatabase(
            productRepository = get()
        )
    }

    single<AddServiceToDatabaseUseCase> {
        AddServiceToDatabase(
            serviceRepository = get()
        )
    }

    single<DeleteServiceFromDatabaseUseCase> {
        DeleteServiceFromDatabase(
            serviceRepository = get()
        )
    }

    single<UpdateServiceToDatabaseUseCase> {
        UpdateServiceToDatabase(
            serviceRepository = get()
        )
    }

    single<AddServiceTypeToDatabaseUseCase> {
        AddServiceTypeToDatabase(
            serviceRepository = get()
        )
    }

    single<DeleteServiceTypeFromDatabaseUseCase> {
        DeleteServiceTypeFromDatabase(
            serviceRepository = get()
        )
    }

    single<UpdateServiceTypeToDatabaseUseCase> {
        UpdateServiceTypeToDatabase(
            serviceRepository = get()
        )
    }

    single<AddEmployeeToDatabaseUseCase> {
        AddEmployeeToDatabase(
            employeeRepository = get()
        )
    }

    single<DeleteEmployeeFromDatabaseUseCase> {
        DeleteEmployeeFromDatabase(
            employeeRepository = get()
        )
    }

    single<UpdateEmployeeToDatabaseUseCase> {
        UpdateEmployeeToDatabase(
            employeeRepository = get()
        )
    }

    single<AddWorkPositionToDatabaseUseCase> {
        AddWorkPositionToDatabase(
            employeeRepository = get()
        )
    }

    single<DeleteWorkPositionFromDatabaseUseCase> {
        DeleteWorkPositionFromDatabase(
            employeeRepository = get()
        )
    }

    single<UpdateWorkPositionToDatabaseUseCase> {
        UpdateWorkPositionToDatabase(
            employeeRepository = get()
        )
    }

    single<CheckEmployeeServiceAuthorizationUseCase> {
        CheckEmployeeServiceAuthorization(
            serviceRepository = get()
        )
    }

    single<AssignServiceToEmployeeUseCase> {
        AssignServiceToEmployee(
            serviceRepository = get()
        )
    }

    single<FinishServiceRequestUseCase> {
        FinishServiceRequest(
            serviceRepository = get()
        )
    }

    single<AddEmployeeSeviceToDatabaseUseCase> {
        AddEmployeeService(
            employeeRepository = get()
        )
    }

    single<DeleteEmployeeServiceFromDatabaseUseCase> {
        DeleteEmployeeService(
            employeeRepository = get()
        )
    }

    single<UpdateEmployeeServiceToDatabaseUseCase> {
        UpdateEmployeeService(
            employeeRepository = get()
        )
    }

}
