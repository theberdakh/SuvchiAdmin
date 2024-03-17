package com.theberdakh.suvchiadmin.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap

@SuppressLint("Range")
fun Uri.getFileName(activity: Activity): String? {
    var result: String? = null
    if (this.scheme == "content") {
        val cursor: Cursor? = activity.contentResolver.query(this, null, null, null, null)
        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }
    if (result == null) {
        result = this.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result!!.substring(cut + 1)
        }
    }
    return result
}

fun Uri.getFileType(activity: Activity): String? {

    val contentResolver = activity.contentResolver
    val mimeType = MimeTypeMap.getSingleton()
    val type = mimeType.getExtensionFromMimeType(contentResolver.getType(this)) // return jpeg
    return contentResolver.getType(this) //Return image/jpeg
}