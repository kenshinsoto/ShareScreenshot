package com.kristianconk.screenshare

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object FileBitmap {

    fun saveBitmapToFile(context: Context, image: Bitmap, filename: String): File {
        val bitmapFile = getOutputMediaFile(context, filename)
            ?: throw NullPointerException("Error creating media file, check storage permissions!")
        val fos = FileOutputStream(bitmapFile)
        image.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        fos.close()

        // se realiza un escaneo de imagenes para que esté disponible
        MediaScannerConnection.scanFile(
            context, arrayOf(bitmapFile.path),
            arrayOf("image/jpeg"), null
        )
        return bitmapFile
    }

    private fun getOutputMediaFile(context: Context, filename: String): File? {
        // TODO verificar la disponibilidad del directorio externo usando Environment.getExternalStorageState()
        val pathName =
            "${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}${File.separator}${getAppName(context)}${File.separator}"
        val mediaStorageDirectory = File(pathName)
        // crear el directorio si no existe
        if (!mediaStorageDirectory.exists()) {
            if (!mediaStorageDirectory.mkdirs()) {
                return null
            }
        }
        // se crea el archivo final
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(Date())
        val mediaFile: File
        val mImageName = "${filename}_$timeStamp.jpg"
        mediaFile = File(mediaStorageDirectory.path + File.separator + mImageName)
        return mediaFile
    }

    private fun getAppName(context: Context): String {
        val pm = context.packageManager
        val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        return pm.getApplicationLabel(appInfo) as String
    }
}