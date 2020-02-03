package ru.shrott.shrottmaster.other.Utils

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore



class Utils(val context: Context) {


    fun getAbsPath(uri: Uri): String? {

        return try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            val picturePath = cursor.getString(columnIndex) // returns null
            cursor.close()
            picturePath

        } catch (e: NullPointerException) {
            uri.path
        }
    }


    // узнаем размеры экрана из класса Display
    fun getDisplayWithWithDP(): Float {
        val displayMetrics = context.resources.displayMetrics
        //var dpHeight = displayMetrics.heightPixels / displayMetrics.density
        return displayMetrics.widthPixels / displayMetrics.density
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = 12f

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }





}