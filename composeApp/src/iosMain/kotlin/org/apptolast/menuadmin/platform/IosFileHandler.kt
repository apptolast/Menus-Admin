package org.apptolast.menuadmin.platform

import org.apptolast.menuadmin.domain.platform.FileHandler

class IosFileHandler : FileHandler {
    override suspend fun pickAndReadFile(): String? {
        // TODO: Implement with UIDocumentPickerViewController
        return null
    }

    override suspend fun saveFile(
        content: String,
        fileName: String,
    ) {
        // TODO: Implement with UIActivityViewController
    }
}
