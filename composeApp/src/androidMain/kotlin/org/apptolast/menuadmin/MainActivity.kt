package org.apptolast.menuadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.apptolast.menuadmin.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (GlobalContext.getOrNull() == null) {
            initKoin { androidContext(this@MainActivity.applicationContext) }
        }
        enableEdgeToEdge()
        setContent { App() }
    }
}
