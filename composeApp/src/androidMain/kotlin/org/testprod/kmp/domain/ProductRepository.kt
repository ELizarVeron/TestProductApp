package org.testprod.kmp.domain

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(id: String): Flow<Product>
}