package org.testprod.kmp.presentation.detail

import androidx.annotation.MainThread
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.testprod.kmp.domain.Product
import org.testprod.kmp.domain.ProductRepository
import org.testprod.kmp.presentation.detail.DetailStore.State

internal interface DetailStore : Store<Nothing, State, Any> {
    sealed interface State {
        data object Loading : State
        data class Success(val prod: Product) : State
        data class Error(val message: String) : State
    }
}

internal class DetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val postRepository: ProductRepository,
) {

    fun create(postId: String): DetailStore =
        object : DetailStore, Store<Nothing, State, Any> by storeFactory.create(
            name = "DetailStore",
            initialState = State.Loading,
            bootstrapper = BootstrapperImpl(
                postId = postId,
                postRepository = postRepository,
            ),
            executorFactory = DetailStoreFactory::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class NewProductLoaded(val post: Product) : Action
        data class ProductLoadFailed(val throwable: Throwable) : Action
    }

    private sealed interface Msg {
        data class UpdateProduct(val post: Product) : Msg
        data class ShowError(val errorText: String) : Msg
    }

    private class BootstrapperImpl(
        private val postId: String,
        private val postRepository: ProductRepository,
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                postRepository.getProductById(postId)
                    .flowOn(Dispatchers.Default)
                    .catch { dispatch(Action.ProductLoadFailed(it)) }
                    .collect { dispatch(Action.NewProductLoaded(it)) }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Nothing, Action, State, Msg, Any>() {
        override fun executeAction(action: Action) {

            when (action) {
                is Action.NewProductLoaded -> {
                    dispatch(Msg.UpdateProduct(action.post))
                }

                is Action.ProductLoadFailed -> {
                    val errorText = action.throwable.message ?: "Failed to load the post"
                    dispatch(Msg.ShowError(errorText))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.ShowError -> State.Error(message.errorText)
                is Msg.UpdateProduct -> State.Success(message.post)
            }
    }
}
