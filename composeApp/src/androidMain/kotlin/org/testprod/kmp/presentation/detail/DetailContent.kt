package org.testprod.kmp.presentation.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.testprod.kmp.domain.Product
import org.testprod.kmp.domain.Rating

val LocalBackPressedHandler = compositionLocalOf<(() -> Unit)?> { null }

@Composable
fun DetailContent(
    component: DetailComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState()

    CompositionLocalProvider(LocalBackPressedHandler provides component::onBackPressed) {
        Scaffold(
            modifier = modifier,
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 16.dp),

            ) {
                item {
                    AnimatedContent(state, label = "") { contentState ->
                        when (contentState) {
                            DetailComponent.Model.Loading -> {

                                Box(
                                    modifier = Modifier

                                        .fillParentMaxSize()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            is DetailComponent.Model.Error -> Text(contentState.errorText)
                            is DetailComponent.Model.Success -> {
                                ProductDetails(contentState.post) // Pass the product details
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailContentPreview() {
    ProductDetails(Product(1, "tedsgsdgdsgsgsgsdgsd", 1.0, "", "", "", Rating(0.1, 1)))
}

@Preview
@Composable
fun ImageInCirclePreview() {

    Box(
        modifier = Modifier.fillMaxWidth().background(Color.Cyan, shape = RoundedCornerShape(10)),
        contentAlignment = Alignment.TopCenter
    ) {

        Box(
            modifier = Modifier

                .background(Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"),
                contentDescription = null,
                modifier = Modifier

                    .background(Color.Red, shape = CircleShape),
                contentScale = ContentScale.Crop
            )
        }


        IconButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Back"
            )
        }


        IconButton(
            onClick = {  },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Call,
                contentDescription = "Like"
            )
        }
    }


}


@Composable
fun ProductDetails(post: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ProductCard(post)
        Title(post.title)
        Info(post.price, post.rating)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(36.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF802C6E)),
            border = BorderStroke(1.dp, Color(0xFF802C6E))
        ) {
            Text(text = "Comprar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Desc(post.description)
    }
}

@Composable
fun ProductCard(post: Product) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFE2DFFC), shape = RoundedCornerShape(10)),
        contentAlignment = Alignment.TopCenter
    ) {

        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(post.image),
                contentDescription = null,

                contentScale = ContentScale.Fit,
                modifier = Modifier.padding(30.dp)

            )
        }
        val onBackPressed = LocalBackPressedHandler.current

        IconButton(
            onClick = { onBackPressed?.invoke() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                tint = Color(0xFF802C6E),
                contentDescription = "Back"
            )
        }


        IconButton(
            onClick = { /* Handle like action */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = org.testprod.kmp.R.drawable.heart),

                contentDescription = "Like"
            )
        }
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h6,
        color = Color(0xFF6A1B9A)
    )
}

@Composable
fun Info(price: Double, rating: Rating) {


    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Space between items
    ) {
        Text(
            text = "Precio:",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.subtitle1,
            color = Color(0xFF6A1B9A)
        )
        Text(
            text = "${price} $",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.subtitle1,
            color = Color(0xFF6A1B9A)
        )
    }



    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Calificaciones: ", color = Color(0xFF802C6E))


        RatingBar(rating = rating.rate.toFloat())
        Text(
            text = rating.rate.toString(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF802C6E)
        )
    }


    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "Votos: ", color = Color(0xFF802C6E))
        Text(
            text = rating.count.toString(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF802C6E)
        )
        Icon(
            imageVector = Icons.Outlined.ThumbUp,
            contentDescription = null,
            tint = Color(0xFF6A1B9A),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun Desc(description: String) {

    Text(
        text = "Descripción",
        style = MaterialTheme.typography.h6,
        color = Color(0xFF6A1B9A)
    )

    Text(
        text = description,
        style = MaterialTheme.typography.body2,
        color = Color(0xFF6A1B9A)
    )
}

@Composable
fun RatingBar(rating: Float) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,

                tint = if (index < rating.toInt()) Color(0xFF6A1B9A) else Color.LightGray
            )
        }
    }
}

@Composable
@Preview
fun ProductCardPreview() {
    ProductCard(
        Product(
            1,
            "tedsgsdgdsgsgsgsdgsd",
            1.0,
            "",
            "",
            "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
            Rating(0.1, 1)
        )
    )
}
