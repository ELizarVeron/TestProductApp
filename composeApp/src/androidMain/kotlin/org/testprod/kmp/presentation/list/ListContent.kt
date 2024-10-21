package org.testprod.kmp.presentation.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import org.testprod.kmp.R
import org.testprod.kmp.domain.Product
import org.testprod.kmp.domain.Rating


@Composable
fun ListContent(
    component: ListComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        topBar = {
            CustomTopBar(
                onBurgerClick = {  },
                onScrollToBottomClick = {
                    coroutineScope.launch {

                        listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1, 0)
                    }
                }
            )
        },

        ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(paddingValues)
        ) {
            items(state) { product ->
                ProductItem(
                    product = product,
                    onBuyClick = { component.onProductClicked(product) },

                    )

            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier,
    onBuyClick: () -> Unit = {},

    ) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onBuyClick() },

        shape = RoundedCornerShape(16.dp),

        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(
                    Color(0xFFE2DFFC),
                    shape = RoundedCornerShape(10)
                )
                .padding(top = 8.dp, start = 8.dp),

            ) {

            IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(32.dp)

            ) {
                Image(
                    painter = painterResource(id = org.testprod.kmp.R.drawable.heart),
                    contentDescription = "Like"
                )
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .padding(10.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = product.image,
                            placeholder = painterResource(id = R.drawable.ph),
                            error = painterResource(id = R.drawable.ph))
                        ,
                        contentDescription = null,


                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(10.dp)
                            .background(Color.White, shape = CircleShape)

                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Bottom,
                    ) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF802C6E),
                        maxLines = 4,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom,

                        ) {
                        Text(
                            text = "$${product.price}",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF802C6E)
                        )

                        OutlinedButton(
                            onClick = { },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .padding(horizontal = 16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(
                                    0xFF802C6E
                                ),
                                backgroundColor = Color.Transparent
                            ),
                            border = BorderStroke(1.dp, Color(0xFF802C6E))
                        ) {
                            Text(text = "Comprar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTopBar(
    onBurgerClick: () -> Unit,
    onScrollToBottomClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "My Awesome App Logo",
                    contentScale = ContentScale.Fit
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBurgerClick) {
                Icon(
                    Icons.Default.Menu,
                    tint = Color(0xFF802C6E),
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = onScrollToBottomClick) {
                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Scroll to Bottom"
                )
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    val exampleProduct = Product(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
        description = "",
        category = "",
        rating = Rating(0.0, 0)
    )

    ProductItem(      product = exampleProduct,
        onBuyClick = { }, )
}