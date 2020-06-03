package com.example.libgen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.libgen.data.models.Book

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Query("SELECT * FROM book_table")
    suspend fun fetchBooks(): List<Book>

    @Query("SELECT * FROM book_table WHERE _id = :bookId")
    suspend fun fetchCovers(bookId: String): Book

}