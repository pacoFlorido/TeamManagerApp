package net.pacofloridoquesada.teammanager.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class ImageUtils {
    companion object {
        fun getRealPathFromURI(contentUri: Uri, context: Context): String {
            var result: String? = null
            val cursor = context.contentResolver.query(contentUri, null, null, null, null)
            if (cursor == null) {
                result = contentUri.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }
            return result ?: ""
        }
    }
}