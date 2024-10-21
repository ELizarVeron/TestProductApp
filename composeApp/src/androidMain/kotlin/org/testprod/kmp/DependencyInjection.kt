package org.testprod.kmp


import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.timetravel.store.TimeTravelStoreFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.testprod.kmp.data.ProductApiService
import org.testprod.kmp.data.ProductApiServiceImpl
import org.testprod.kmp.data.ProductRepositoryImpl
import org.testprod.kmp.domain.ProductRepository
import org.testprod.kmp.presentation.detail.DetailComponent
import org.testprod.kmp.presentation.detail.DetailStoreFactory
import org.testprod.kmp.root.DefaultRootComponent
import org.testprod.kmp.root.RootComponent
import org.testprod.kmp.presentation.detail.DefaultDetailComponent
import org.testprod.kmp.presentation.list.DefaultListComponent
import org.testprod.kmp.presentation.list.ListStoreFactory
import org.testprod.kmp.presentation.list.ListComponent


val kodeinDI = DI {

    bindSingleton<HttpClient>()  {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    bindSingleton<ProductApiService>()   { ProductApiServiceImpl(instance()) }

    // repository
    bindSingleton<ProductRepository> { ProductRepositoryImpl(instance()) }

    // store
    bindSingleton<StoreFactory> {
        LoggingStoreFactory(TimeTravelStoreFactory())
    }

    // detail
    bindSingleton<DetailComponent.Factory> {
        DefaultDetailComponent.Factory(
            detailStoreFactory = instance(),
        )
    }
    bindSingleton {
        DetailStoreFactory(
            storeFactory = instance(),
            postRepository = instance(),
        )
    }

    // list
    bindSingleton<ListComponent.Factory> {
        DefaultListComponent.Factory(
            listStoreFactory = instance(),
        )
    }
    bindSingleton {
        ListStoreFactory(
            storeFactory = instance(),
            postRepository = instance(),
        )
    }

    // root
    bindSingleton<RootComponent.Factory> {
        DefaultRootComponent.Factory(
            detailComponentFactory = instance(),
            listComponentFactory = instance(),
        )
    }
}