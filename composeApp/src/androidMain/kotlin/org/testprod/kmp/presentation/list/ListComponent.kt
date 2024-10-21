package org.testprod.kmp.presentation.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.testprod.kmp.domain.Product

interface ListComponent {
    val model: Value<List<Product>>

    fun onProductClicked(post: Product)
    fun fabClicked()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            postClicked: (postId: String) -> Unit,

        ): ListComponent
    }
}