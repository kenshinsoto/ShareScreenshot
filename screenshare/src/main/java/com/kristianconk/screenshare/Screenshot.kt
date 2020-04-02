package com.kristianconk.screenshare

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

object Screenshot {

    fun screenshotOfView(view: View, desiredWidth: Int = 0, desiredHeight: Int = 0): Bitmap {
        if (desiredWidth > 0 && desiredHeight > 0) {
            val density = view.resources.displayMetrics.density.toInt()
            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    desiredWidth * density,
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    desiredHeight * density,
                    View.MeasureSpec.EXACTLY
                )
            )
        }
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.background?.draw(canvas)
        view.draw(canvas)
        return bitmap
    }
}