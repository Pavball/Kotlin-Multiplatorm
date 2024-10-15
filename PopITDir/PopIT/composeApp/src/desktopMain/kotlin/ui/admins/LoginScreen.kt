package ui.admins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import appDesign.PopItTheme
import domain.model.EmployeeType
import org.koin.compose.viewmodel.koinViewModel
import ui.viewmodels.LoginScreenViewModel
import ui.viewmodels.LoginScreenViewState

@Composable
internal fun LoginScreen(
    onLoginSuccess: (Int, String, Boolean, EmployeeType) -> Unit
) {
    val loginScreenViewModel = koinViewModel<LoginScreenViewModel>()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    var attemptLogin by remember { mutableStateOf(false) }

    val viewLoginState by loginScreenViewModel.viewState<LoginScreenViewState.LoginSection>()
        .collectAsState(initial = LoginScreenViewState.LoginSection.initial)

    println("EMPLOYEE NAME: " + viewLoginState.employeeDetail.name)

    val currentOnLoginSuccess by rememberUpdatedState(onLoginSuccess)

    LaunchedEffect(attemptLogin, viewLoginState) {
        if (attemptLogin) {
            if (viewLoginState.employeeDetail.name.isNotEmpty()) {
                val loggedInUserType =
                    if (isAdmin) EmployeeType.Admin else EmployeeType.Employee
                val employeeId = viewLoginState.employeeDetail.employeeId
                val employeeName = viewLoginState.employeeDetail.name
                currentOnLoginSuccess(
                    employeeId,
                    employeeName,
                    isAdmin,
                    loggedInUserType
                )
            } else {
                errorMessage = "Krivi email, lozinka ili tip korisnika!"
            }
            attemptLogin = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Admin/Employee Login",
            style = PopItTheme.typography.titleBold24
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = ""
            },
            label = { Text("Email") },
        )
        Spacer(modifier = Modifier.height(16.dp))

        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Lozinka") },
            singleLine = true,
            placeholder = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Done
                else Icons.Filled.Lock

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isAdmin,
                onCheckedChange = { isAdmin = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Admin")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = PopItTheme.colors.error,
                style = PopItTheme.typography.button16
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Molimo upišite email i lozinku!"
                } else {
                    errorMessage = ""
                    attemptLogin = true
                    loginScreenViewModel.authenticateUser(email, password, isAdmin)

                }
            },
            colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
        ) {
            Text(text = "Prijava", color = PopItTheme.colors.background)
        }
    }
}

