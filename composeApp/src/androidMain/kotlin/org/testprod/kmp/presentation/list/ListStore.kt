package org.testprod.kmp.presentation.list

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.testprod.kmp.domain.Product
import org.testprod.kmp.domain.ProductRepository
import org.testprod.kmp.presentation.list.ListStore.State

internal interface ListStore : Store<Nothing, State, Any> {
    data class State(
        val items: List<Product> = emptyList()
    )
}

internal class ListStoreFactory(
    private val storeFactory: StoreFactory,
    private val postRepository: ProductRepository,
) {

    fun create(): ListStore =
        object : ListStore, Store<Nothing, State, Any> by storeFactory.create(
            name = "ListStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(postRepository),
            executorFactory = ListStoreFactory::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class NewItemsReceived(val items: List<Product>) : Action
    }

    private sealed interface Msg {
        data class UpdateItems(val items: List<Product>) : Msg
    }

    private class BootstrapperImpl(
        private val repository: ProductRepository,
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                repository.getAllProducts()
                    .flowOn(Dispatchers.Default)
                    .collect { items ->
                        dispatch(Action.NewItemsReceived(items))
                    }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Nothing, Action, State, Msg, Any>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.NewItemsReceived -> {
                    dispatch(Msg.UpdateItems(action.items))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.UpdateItems -> copy(items = message.items)
            }
    }
}
