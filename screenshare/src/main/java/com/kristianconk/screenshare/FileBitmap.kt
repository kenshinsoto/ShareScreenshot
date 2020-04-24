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

    /**
     * Guarda un bitmap en el sistema de archivos de la aplicación
     *
     * @param context Contexto de la aplicacion requerido para acceder al media system
     * @param image Bitmap a guardar
     * @param filename nombre de la imagen a guardar
     * @return File de la imagen guardada
     */
    fun saveBitmapToFile(context: Context, image: Bitmap, filename: String): File {
        val bitmapFile = getOutputMediaFile(context, filename)
            ?: throw NullPointerException("Error creating media file, check storage permissions!")
        val fos = FileOutputStream(bitmapFile)
        image.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        fos.close()
        return bitmapFile
    }

    /**
     * Genera el archivo en la carpeta de contenido fotos de la aplicación
     *
     * @param context Requerido para acceder al sistema de archivos
     * @param filename nombre con el que se va a guardar
     * @return Archivo si es que se pudo crear
     */
    private fun getOutputMediaFile(context: Context, filename: String): File? {
        val pathName =
            "${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath}${File.separator}${getAppName(
                context
            )}${File.separator}"
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

    /**
     * Recupera el nombre de la aplicación
     *
     * @param context Requerido para acceder al package manager
     * @return Nombre de la aplicación actual
     */
    private fun getAppName(context: Context): String {
        val pm = context.packageManager
        val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        return pm.getApplicationLabel(appInfo) as String
    }
}