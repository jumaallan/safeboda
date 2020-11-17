package com.safeboda.core.store

import android.content.Context
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * Holds an in-memory copy of our recent searches saved to disk.
 */
class RecentSearchStore(context: Context) {

    companion object {
        private const val CAPACITY = 15
        private const val FILENAME = "recent-searches.txt"

        @WorkerThread
        private fun readFile(file: File): List<String> {
            val searches = mutableListOf<String>()
            try {
                if (!file.exists()) file.createNewFile()
                searches.addAll(file.readLines().toMutableList())
                Timber.d("Read ${searches.size} recent searches from disk.")
            } catch (ex: IOException) {
                Timber.e(ex, "Couldn't read from recent search file.")
            }
            return searches
        }

        @WorkerThread
        private fun writeFile(file: File, searches: List<String>) {
            try {
                if (searches.isEmpty()) {
                    file.delete()
                } else {
                    file.writeText(searches.joinToString("\n"))
                }
                Timber.d("Wrote ${searches.size} recent searches to disk.")
            } catch (ex: IOException) {
                Timber.e(ex, "Couldn't write to recent search file.")
            }
        }

        @UiThread
        fun delete(context: Context, viewModelScope: CoroutineScope) {
            val file = File(context.filesDir, FILENAME)
            viewModelScope.launch(Dispatchers.IO) {
                delete(file)
            }
        }

        @WorkerThread
        private fun delete(file: File) {
            try {
                file.delete()
                Timber.d("Deleted recent searches.")
            } catch (ex: IOException) {
                Timber.e("Couldn't delete recent searches.")
            }
        }
    }

    /**
     * Any modifying of storage should be synchronized on this lock.
     */
    private val lock = Any()
    private val storage: MutableList<String> = mutableListOf()
    private val file = File(context.filesDir, FILENAME)

    val recentSearches: List<String>
        @UiThread
        get() {
            var clone: List<String>
            synchronized(lock) { clone = ArrayList(storage) }
            return clone.take(CAPACITY)
        }

    /**
     * Replace our in-memory copy with the searches on disk.
     */
    @WorkerThread
    fun readFromDisk() {
        val searches = readFile(file)
        synchronized(lock) {
            storage.clear()
            storage.addAll(searches)
        }
    }

    /**
     * Replace our on-disk copy with the searches in memory.
     */
    @WorkerThread
    fun insert(searchQuery: String) {
        synchronized(lock) {
            if (searchQuery.isBlank() || storage.firstOrNull() == searchQuery) return
            if (storage.contains(searchQuery)) {
                storage.remove(searchQuery)
                storage.add(0, searchQuery)
            } else {
                storage.add(0, searchQuery)
            }
        }
        writeFile(file, recentSearches)
    }

    /**
     * Replace our on-disk copy with the searches in memory.
     */
    @WorkerThread
    fun remove(searchQuery: String) {
        synchronized(lock) {
            storage.remove(searchQuery)
        }
        writeFile(file, recentSearches)
    }

    @WorkerThread
    fun delete() {
        synchronized(lock) {
            storage.clear()
        }
        delete(file)
    }
}