package ru.shrott.shrottmaster.other.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WorkWithPhotos(var context: Context) {

    companion object {
        private const val IMAGE_DIRECTORY_NAME = "Shrott Camera"
    }

    private fun resizingPhoto(uriFile: Uri): BitmapFactory.Options {

        val cr = context.contentResolver
        val input = cr.openInputStream(uriFile)

        var o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        //BitmapFactory.decodeFile(patchFile, o)
        BitmapFactory.decodeStream(input, null, o)

        val origWidth = o.outWidth //исходная ширина
        val origHeight = o.outHeight //исходная высота
        val bytesPerPixel = 2 //соответствует RGB_555 конфигурации
        var maxSize = 810 * 1080 * bytesPerPixel //Максимально разрешенный размер Bitmap
        val desiredWidth = 810 //Нужная ширина
        val desiredHeight = 1080 //Нужная высота
        val desiredSize =
            desiredWidth * desiredHeight * bytesPerPixel //Максимально разрешенный размер Bitmap для заданных width х height
        if (desiredSize < maxSize) maxSize = desiredSize
        var scale = 1 //кратность уменьшения
        val origSize = origWidth * origHeight * bytesPerPixel
        //высчитываем кратность уменьшения
        scale = if (origWidth > origHeight) {
            Math.round(origHeight.toFloat() / desiredHeight.toFloat())
        } else {
            Math.round(origWidth.toFloat() / desiredWidth.toFloat())
        }

        o = BitmapFactory.Options()
        o.inSampleSize = scale
        o.inPreferredConfig = Bitmap.Config.RGB_565

        return o
    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {

        val cr = context.contentResolver
        val input = cr.openInputStream(uri)

        return BitmapFactory.decodeStream(input, null, resizingPhoto(uri))
    }


    fun newFileUri(): Uri? {
        val file = getOutputMediaFile()
        return if (file != null)
            FileProvider
                .getUriForFile(context, "${context.applicationContext.packageName}.provider", file)
        else null
    }

    private fun getOutputMediaFile(): File? {

        // External sdcard location
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val mediaFile: File

        mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")

        return mediaFile
    }


    fun imageToBase64(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 65, byteArrayOutputStream)

        val bytes = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}