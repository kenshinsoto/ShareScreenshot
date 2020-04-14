package com.kristianconk.screenshare

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.content.FileProvider
import com.kristianconk.screenshare.Screenshot.screenshotOfView
import java.io.File

object Share {

    fun shareImage(
        activity: Activity,
        file: File,
        message: String?
    ) {
        //debido a restriciones de file:// en la api 24+ se debe calcular un nuevo uri tipo content://
        val apkUri = FileProvider.getUriForFile(
            activity,
            activity.applicationContext.packageName + ".provider",
            file
        )
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        message?.let {
            if (it.isNotEmpty()) {
                shareIntent.putExtra(Intent.EXTRA_TEXT, it)
            }
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, apkUri)
        shareIntent.type = "image/jpeg"
        //Validación de permisos para android Lollipop o posterior y sus predecesores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            val resInfoList = activity.packageManager
                .queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                activity.grantUriPermission(
                    packageName,
                    apkUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
        activity.startActivity(Intent.createChooser(shareIntent, "Share"))
    }

    fun shareScreenshot(
        activity: Activity,
        message: String?,
        view: View,
        desiredWidth: Int = 0,
        desiredHeight: Int = 0
    ) {
        val bitmapFromView = screenshotOfView(view, desiredWidth, desiredHeight)
        val fileToShare = FileBitmap.saveBitmapToFile(activity, bitmapFromView, "screenshot")
        shareImage(activity, fileToShare, message)
    }
}