package org.testprod.kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import org.kodein.di.instance
import org.testprod.kmp.root.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponentFactory: RootComponent.Factory by kodeinDI.instance()
        val rootComponent = rootComponentFactory(defaultComponentContext())

        setContent {

            App(rootComponent = rootComponent)
        }

    }

}

