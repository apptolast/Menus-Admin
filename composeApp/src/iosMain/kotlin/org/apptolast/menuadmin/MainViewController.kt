package org.apptolast.menuadmin

import androidx.compose.ui.window.ComposeUIViewController
import org.apptolast.menuadmin.di.initKoin
import org.koin.core.context.GlobalContext

fun MainViewController() =
    ComposeUIViewController(
        configure = {
            if (GlobalContext.getOrNull() == null) {
                initKoin()
            }
        },
    ) {
        App()
    }
