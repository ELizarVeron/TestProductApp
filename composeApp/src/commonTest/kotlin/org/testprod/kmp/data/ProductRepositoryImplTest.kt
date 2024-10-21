package org.testprod.kmp.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.testprod.kmp.domain.Product
import org.testprod.kmp.domain.Rating


class ProductRepositoryImplTest {

    private lateinit var apiService: ProductApiService
    private lateinit var repository: ProductRepositoryImpl



    @Before
    fun setup() {
        apiService = mock()
        repository = ProductRepositoryImpl(apiService)
    }

    @org.junit.Test
    fun getAllProducts_shouldReturnAListOfProducts() = runTest {
        val productDtos = listOf(
            ProductDto(1,"tedsgsdgdsgsgsgsdgsd",1.0,"", "", "", RatingDto(0.1,1)),
            ProductDto(2,"dsoisfiopeifeso",1.0,"", "", "", RatingDto(0.1,1))

        )
        val expectedProducts = productDtos.map { it.mapToProduct() }

        whenever(apiService.fetchAllProducts()).thenReturn(flowOf(productDtos))

        val actualProducts = repository.getAllProducts().first()

        org.junit.Assert.assertEquals(expectedProducts, actualProducts)
    }

    @org.junit.Test
    fun getProductById_shouldReturnproduct() = runTest {
        val productId = 123
        val productDto =   ProductDto(productId,"tedsgsdgdsgsgsgsdgsd",1.0,"", "", "", RatingDto(0.1,1))

        val expectedProduct = productDto.mapToProduct()

        whenever(apiService.fetchProductById(productId.toString())).thenReturn(flowOf(productDto))

        val actualProduct = repository.getProductById(productId.toString()).first()

        org.junit.Assert.assertEquals(expectedProduct, actualProduct)
    }

}