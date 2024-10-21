package org.testprod.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.testprod.kmp.presentation.ui.theme.AppTheme
import org.testprod.kmp.root.RootComponent
import org.testprod.kmp.root.RootContent

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    AppTheme {
        RootContent(
            component = rootComponent,
            modifier = Modifier.fillMaxSize(),
        )
    }
}