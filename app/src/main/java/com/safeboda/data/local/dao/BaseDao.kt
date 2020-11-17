package com.safeboda.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg items: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: T): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(items: List<T>): Int

    @Delete
    suspend fun delete(item: T)
}