package com.example.libgen.utli

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create a File for saving an image or video
 */
fun getOutputMediaFile(context: Context, directory: String): File {
    val mediaStorageDir =
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), directory).apply {
            if (!exists()) {
                mkdir()
            }
        }

    // Create a media file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    return File.createTempFile("IMG_${timeStamp}", ".jpg", mediaStorageDir)
}
