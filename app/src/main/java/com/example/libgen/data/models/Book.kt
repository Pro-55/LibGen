package com.example.libgen.data.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class Book(

    @PrimaryKey(autoGenerate = false)
    var _id: String = "",

    var name: String = "",

    var author: String = "",

    var price: String = "",

    var doi: Long = 0L,

    var covers: String = ""

) {

    fun getSafeCovers(): List<Uri> = covers.split(",").map { Uri.parse(it.trim()) }

}