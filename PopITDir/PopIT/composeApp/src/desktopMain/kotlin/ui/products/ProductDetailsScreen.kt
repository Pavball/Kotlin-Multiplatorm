package ui.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appDesign.PopItTheme
import org.jetbrains.skia.Image
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import ui.viewmodels.ProductDetailsScreenViewModel
import ui.viewmodels.ProductDetailsScreenViewState
import java.net.URL

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    productId: Int
) {
    val productDetailsScreenViewModel = koinViewModel<ProductDetailsScreenViewModel> {
        parametersOf(productId)
    }

    val viewProductDetailsState by productDetailsScreenViewModel.viewState<ProductDetailsScreenViewState.ProductDetailsSection>()
        .collectAsState(initial = ProductDetailsScreenViewState.ProductDetailsSection.initial)

    Column(
        modifier = Modifier
            .padding(PopItTheme.spacing.medium16)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Network Image Section
        val imageUrl = viewProductDetailsState.productDetail.productImage
        NetworkImage(url = imageUrl)

        Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

        // Order Info Section
        Column(
            modifier = Modifier
                .padding(PopItTheme.spacing.medium16)
                .background(PopItTheme.colors.primary)
                .padding(PopItTheme.spacing.medium16)
                .fillMaxWidth()
        ) {
            Text(
                text = "Product ID: ${viewProductDetailsState.productDetail.productId}",
                style = PopItTheme.typography.titleBold24,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            Text(
                text = "Product Name: ${viewProductDetailsState.productDetail.productName}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            Text(
                text = "Product Desc: ${viewProductDetailsState.productDetail.productDesc}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            Text(
                text = "Total: ${viewProductDetailsState.productDetail.productPrice} €",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            Text(
                text = "Product Stock: ${viewProductDetailsState.productDetail.productStock} in stock",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))

            Divider(color = PopItTheme.colors.background, thickness = 1.dp)
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))
            Text(
                text = "Product Category Name: ${viewProductDetailsState.productDetail.productCategory.categoryName}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))
            Text(
                text = "Product Category Desc: ${viewProductDetailsState.productDetail.productCategory.categoryDesc}",
                style = PopItTheme.typography.button16,
                color = PopItTheme.colors.background
            )
            Spacer(modifier = Modifier.height(PopItTheme.spacing.small8))
        }

        Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

        TextButton(
            onClick = { navController.navigateUp() },
            colors = ButtonDefaults.buttonColors(PopItTheme.colors.primary)
        ) {
            Text(text = "Back to Products", color = PopItTheme.colors.background)
        }
    }
}

@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    var image by remember { mutableStateOf<Image?>(null) }

    // Dodajte provjeru i dodajte "https://" ako URL nema protokol
    val validUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
        url
    } else {
        "https://$url"
    }

    LaunchedEffect(validUrl) {
        try {
            val bytes = URL(validUrl).readBytes()
            image = Image.makeFromEncoded(bytes)
        } catch (e: Exception) {
            e.printStackTrace() // Rukovanje greškom
        }
    }

    image?.let {
        Image(
            painter = BitmapPainter(it.toComposeImageBitmap()),
            contentDescription = null,
            modifier = modifier
                .height(250.dp)
                .aspectRatio(ratio = 16f / 10f)
                .clip(RoundedCornerShape(PopItTheme.spacing.medium16)),
            contentScale = ContentScale.Crop
        )
    }
}
