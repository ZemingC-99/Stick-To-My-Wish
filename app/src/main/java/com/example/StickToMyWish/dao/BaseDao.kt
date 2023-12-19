//References: https://developer.android.com/jetpack/androidx/releases/room
package com.example.StickToMyWish.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

// Generic DAO (Data Access Object) interface providing basic CRUD operations for a given entity type T.
@Dao
interface BaseDao<T> {
    @Insert
    fun insert(bean: T)

    @Insert
    fun insertAll(bean: T)

    @Delete
    fun delete(bean: T)

    @Update
    fun update(bean: T)
}