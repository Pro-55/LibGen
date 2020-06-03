package com.example.libgen.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libgen.data.local.AppDatabase
import com.example.libgen.data.models.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {

    private var db: AppDatabase? = null
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    fun addBook(book: Book) = runBlocking(Dispatchers.IO) { db?.libraryDao?.insertBook(book) }

    fun getBooks() = viewModelScope.launch(Dispatchers.IO) {
        db?.libraryDao?.fetchBooks()?.let { _books.postValue(it) }
    }

    fun getCovers(_id: String): LiveData<List<Uri>> {
        val covers = MutableLiveData<List<Uri>>()
        viewModelScope.launch(Dispatchers.IO) {
            val data = db?.libraryDao?.fetchCovers(_id)?.getSafeCovers()
            if (!data.isNullOrEmpty()) covers.postValue(data)
        }
        return covers
    }

    fun setAppDB(db: AppDatabase) {
        this.db = db
    }

}