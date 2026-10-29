package com.example.reater.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object FilePreviewHelper {

    fun openFile(context: Context, rawPathOrUrl: String?) {
        if (rawPathOrUrl.isNullOrBlank()) {
            Toast.makeText(context, "No file attachment found", Toast.LENGTH_SHORT).show()
            return
        }

        val fullUrl = resolveFullUrl(rawPathOrUrl)
        val uri = Uri.parse(fullUrl)

        // 1. Try opening via explicit PDF viewer intent
        val pdfIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(pdfIntent)
            return
        } catch (e: ActivityNotFoundException) {
            // PDF viewer app not found, fall back to browser
        } catch (e: Exception) {
            // Fall back to browser
        }

        // 2. Fall back to generic browser / viewer intent
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "No app found to open this document. Please install a PDF reader (e.g. Google Drive or Adobe Reader).",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun resolveFullUrl(rawPath: String): String {
        val trimmed = rawPath.trim()
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("content://") || trimmed.startsWith("file://")) {
            return trimmed
        }
        val cleanPath = trimmed.removePrefix("/")
        val base = if (Constants.BASE_URL.endsWith("/")) Constants.BASE_URL else "${Constants.BASE_URL}/"
        return base + cleanPath
    }
}
