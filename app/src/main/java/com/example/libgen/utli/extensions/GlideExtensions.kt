package com.example.libgen.utli.extensions

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun Fragment.glide() = Glide.with(this)


fun Bitmap.toFile(file: File): File {
    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 90, bos)
    val bitmapData = bos.toByteArray()

    // Write the bytes in file
    val fos = FileOutputStream(file)
    fos.write(bitmapData)
    fos.flush()
    fos.close()
    return file
}

fun Uri.toBitmap(contentResolver: ContentResolver): Bitmap {
    val parcelFileDescriptor = contentResolver.openFileDescriptor(this, "r")
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return image
}