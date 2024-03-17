package com.theberdakh.suvchiadmin.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.theberdakh.suvchiadmin.R

fun Activity.vibratePhone(milliSeconds: Long = 100) {
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(milliSeconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(100)
    }
}

fun Activity.downloadFile( url: String, fileTitle: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && // 1.
        ContextCompat.checkSelfPermission( // 2.
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions( // 3.
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )

        showToast(getString(R.string.permission_denied_try_again))
        return
    }

    val request = DownloadManager.Request(Uri.parse(url)) // 5.
        .setNotificationVisibility( // 6.
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        .setDestinationInExternalPublicDir( // 7.
            Environment.DIRECTORY_DOWNLOADS, fileTitle
        )

    val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request) // 8.
    showToast(getString(R.string.download_started))
}

fun Activity.enterFullScreen() {
    this.window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Activity.exitFullScreen(){
    this.window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
}