package org.testprod.kmp.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import org.testprod.kmp.presentation.list.ListComponent
import org.testprod.kmp.presentation.detail.DetailComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class List(val component: ListComponent) : Child
        class Detail(val component: DetailComponent) : Child

    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RootComponent
    }
}