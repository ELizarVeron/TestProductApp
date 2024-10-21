package org.testprod.kmp.presentation.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import org.testprod.kmp.domain.Product
import org.testprod.kmp.util.asValue

internal class DefaultListComponent(
    componentContext: ComponentContext,
    private val listStoreFactory: ListStoreFactory,
    private val postClicked: (postId: String) -> Unit,

) : ListComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { listStoreFactory.create() }

    override val model: Value<List<Product>> = store.asValue().map { it.items }

    override fun onProductClicked(post: Product) = postClicked(post.id.toString())
    override fun fabClicked() {

    }


    class Factory(
        private val listStoreFactory: ListStoreFactory
    ) : ListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            postClicked: (postId: String) -> Unit,

        ): ListComponent {
            return DefaultListComponent(
                componentContext = componentContext,
                postClicked = postClicked,
                listStoreFactory = listStoreFactory,

            )
        }
    }
}
