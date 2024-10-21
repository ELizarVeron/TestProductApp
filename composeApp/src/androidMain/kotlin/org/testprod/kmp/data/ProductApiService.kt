package org.testprod.kmp.data

import android.media.Rating
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.serialization.Serializable
import org.testprod.kmp.domain.Product

interface ProductApiService {
    suspend fun fetchAllProducts(): Flow<List<ProductDto>>
    suspend fun fetchProductById(id: String):  Flow<ProductDto>
}

class ProductApiServiceImpl(
    private val client: HttpClient
) : ProductApiService {
    override suspend fun fetchAllProducts(): Flow<List<ProductDto>> = flow {
        val products = client.get("https://fakestoreapi.com/products").body<List<ProductDto>>()
        emit(products)
    }

    override suspend fun fetchProductById(id: String): Flow<ProductDto> = flow {
        val product = client.get("https://fakestoreapi.com/products/$id").body<ProductDto>()
        emit(product)
    }
}

@Serializable
data class ProductDto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto
)

@Serializable
data class RatingDto(
    val rate: Double,
    val count: Int
)

fun ProductDto.mapToProduct(): Product {
    return Product(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        category = this.category,
        image = this.image,
        rating = org.testprod.kmp.domain.Rating(
            rate = this.rating.rate,
            count = this.rating.count
        )
    )
}