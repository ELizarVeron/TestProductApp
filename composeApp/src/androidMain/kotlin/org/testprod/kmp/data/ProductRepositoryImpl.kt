package org.testprod.kmp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.testprod.kmp.domain.Product
import org.testprod.kmp.domain.ProductRepository

class ProductRepositoryImpl(
    private val apiService: ProductApiService
) : ProductRepository {
    private val productCache = mutableMapOf<String, ProductDto>()
    override suspend fun getAllProducts(): Flow<List<Product>> {
        return apiService.fetchAllProducts().map { list ->
            list.map { dto ->
                dto.mapToProduct()
            }
        }
    }
    override suspend fun getProductById(id: String): Flow<Product> {
        return flow {
            productCache[id]?.let { cachedProduct ->
                emit(cachedProduct.mapToProduct())
                return@flow
            }
            val product = apiService.fetchProductById(id).first()
            productCache[id] = product
            emit(product.mapToProduct())
        }
    }


}
