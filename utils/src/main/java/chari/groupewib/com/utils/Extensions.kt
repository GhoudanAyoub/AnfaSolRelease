package ghoudan.anfaSolution.com.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun Bitmap.toFile(context: Context, fileName: String): File? {
    var file: File? = null

    return try {
        file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName).apply {
            createNewFile()
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bitmapData = byteArrayOutputStream.toByteArray()

        //write the bytes in file
        FileOutputStream(file).apply {
            write(bitmapData)
            flush()
            close()
        }

        file
    } catch (e: Exception) {
        Timber.e(e)
        file
    }
}
