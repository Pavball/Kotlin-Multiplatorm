package ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import appDesign.PopItTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import ui.viewmodels.HomeScreenViewModel
import ui.viewmodels.HomeScreenViewState

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    employeeId: Int,
    isAdmin: Boolean
) {
    val homeScreenViewModel = koinViewModel<HomeScreenViewModel>() {
        parametersOf(employeeId, isAdmin)
    }

    val viewHomeState by homeScreenViewModel.viewState<HomeScreenViewState.HomeSection>()
        .collectAsState(initial = HomeScreenViewState.HomeSection.initial)

    val viewHomeOrdersState by homeScreenViewModel.viewState<HomeScreenViewState.OrderNumbersSection>()
        .collectAsState(initial = HomeScreenViewState.OrderNumbersSection.initial)

    val viewHomeServicesState by homeScreenViewModel.viewState<HomeScreenViewState.ServicesNumbersSection>()
        .collectAsState(initial = HomeScreenViewState.ServicesNumbersSection.initial)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(modifier = Modifier
            .height(149.dp)
            .background(PopItTheme.colors.primary)
            .fillMaxWidth()
        ){
            Divider(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter),
                color = Color.White,
                thickness = 1.dp
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ){
            Text(text = "Dobrodošli, ${viewHomeState.employeeDetail.name}!", style = MaterialTheme.typography.h4)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "${viewHomeState.employeeDetail.name} ${viewHomeState.employeeDetail.surname}")
            Text(text = "Telefon: ${viewHomeState.employeeDetail.phoneNumber}")

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column {
                    Text(text = "Zatražene Narudžbe", style = MaterialTheme.typography.h6)
                    Text(text = "${viewHomeOrdersState.totalOrders.size}")
                }

                Column {
                    Text(text = "Zatraženi Servisi", style = MaterialTheme.typography.h6)
                    Text(text = "${viewHomeServicesState.totalServices.size}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }


    }
}
