package net.aucutt.lewinesnob.data

import android.content.Context
import androidx.core.net.toUri
import java.io.File
import java.io.InputStream

class WineImageStore(private val context: Context) {
    private val photosDir: File
        get() = File(context.filesDir, "wine_photos").also { it.mkdirs() }

    fun persist(wineId: String, uriString: String?): String? {
        if (uriString.isNullOrBlank()) return null
        val dest = File(photosDir, "$wineId.jpg")
        val sourceUri = uriString.toUri()
        if (sourceUri.scheme == "file" && sourceUri.path == dest.absolutePath && dest.exists()) {
            return dest.toURI().toString()
        }
        open(uriString)?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        } ?: return uriString
        return dest.toURI().toString()
    }

    fun open(uriString: String): InputStream? {
        val uri = uriString.toUri()
        return runCatching {
            when (uri.scheme) {
                "content" -> context.contentResolver.openInputStream(uri)
                "file" -> uri.path?.let { path -> File(path).takeIf { it.exists() }?.inputStream() }
                else -> File(uriString).takeIf { it.exists() }?.inputStream()
            }
        }.getOrNull()
    }
}
