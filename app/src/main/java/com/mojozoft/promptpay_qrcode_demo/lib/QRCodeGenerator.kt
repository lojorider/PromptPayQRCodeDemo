package com.mojozoft.qrcodedemo.lib

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import android.view.View
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Created by Lojorider on 10/17/2017.
 */
class QRCodeGenerator(var code: String, var size: Int, private var background: Int, private var foreground: Int) {

    init {
        setCode()
    }

    lateinit var bitMatrix: BitMatrix
    var myBitmap: Bitmap? = null
    @Throws(WriterException::class)
    fun setCode() {
        try {
            bitMatrix = MultiFormatWriter().encode(code, BarcodeFormat.QR_CODE, this.size, this.size, null
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
            return
        }
        val bitMatrixWidth = bitMatrix.width
        val bitMatrixHeight = bitMatrix.height
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) foreground else background
            }
        }
        myBitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        myBitmap?.setPixels(pixels, 0, size, 0, 0, bitMatrixWidth, bitMatrixHeight)
    }
    fun getBitmap(): Bitmap? {
        return myBitmap
    }
//
//    fun saveImageAs(context: Context, image_directory: String): String {
//
//        val bytes = ByteArrayOutputStream()
//        myBitmap?.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
//        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + image_directory)
//        // have the object build the directory structure, if needed.
//
//        if (!wallpaperDirectory.exists()) {
//            Log.d("mojolog", "" + wallpaperDirectory.mkdirs())
//            wallpaperDirectory.mkdirs()
//        }
//        Log.d("mojolog", Environment.getExternalStorageDirectory().toString())
//        try {
//            val f = File(wallpaperDirectory, Calendar.getInstance().timeInMillis.toString() + ".jpg")
//            Log.d("mojolog", Calendar.getInstance().timeInMillis.toString() + ".jpg")
//            f.createNewFile()   //give read write permission
//            val fo = FileOutputStream(f)
//            fo.write(bytes.toByteArray())
//            MediaScannerConnection.scanFile(context, arrayOf(f.getPath()), arrayOf("image/jpeg"), null)
//            fo.close()
//            Log.d("mojolog", "File Saved::--->" + f.getAbsolutePath())
//
//            return f.getAbsolutePath()
//        } catch (e1: IOException) {
//            Log.d("mojolog", "Error")
//            e1.printStackTrace()
//        }
//
//        return ""
//
//    }
}