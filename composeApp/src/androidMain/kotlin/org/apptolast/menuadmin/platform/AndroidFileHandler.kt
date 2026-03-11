package org.apptolast.menuadmin.platform

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apptolast.menuadmin.domain.platform.FileHandler
import kotlin.coroutines.resume

class AndroidFileHandler(
    private val context: Context,
) : FileHandler {
    override suspend fun pickAndReadFile(): String? {
        val activity = context as? ComponentActivity ?: return null

        val uri = pickFile(activity) ?: return null

        return try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                stream.bufferedReader().readText()
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun saveFile(
        content: String,
        fileName: String,
    ) {
        val activity = context as? ComponentActivity ?: return

        val uri = createFile(activity, fileName) ?: return

        try {
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(content.toByteArray())
            }
        } catch (_: Exception) {
            // Silently fail
        }
    }

    private suspend fun pickFile(activity: ComponentActivity): Uri? =
        suspendCancellableCoroutine { cont ->
            val launcher = activity.activityResultRegistry.register(
                "pick_file",
                ActivityResultContracts.OpenDocument(),
            ) { uri ->
                cont.resume(uri)
            }
            launcher.launch(arrayOf("application/json"))
        }

    private suspend fun createFile(
        activity: ComponentActivity,
        fileName: String,
    ): Uri? =
        suspendCancellableCoroutine { cont ->
            val launcher = activity.activityResultRegistry.register(
                "create_file",
                ActivityResultContracts.CreateDocument("application/json"),
            ) { uri ->
                cont.resume(uri)
            }
            launcher.launch(fileName)
        }
}
