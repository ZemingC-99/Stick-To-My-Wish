package com.example.StickToMyWish

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.StickToMyWish.dao.ProjectDao
import com.example.StickToMyWish.dao.RecordDao
import com.example.StickToMyWish.dao.TabDao
import com.example.StickToMyWish.entity.Project
import com.example.StickToMyWish.entity.Record
import com.example.StickToMyWish.entity.Tab

@Database(
    entities = [Tab::class, Project::class, Record::class], version = 1, exportSchema = false
)
abstract class MyDataBase : RoomDatabase() {

    abstract fun tabDao(): TabDao
    abstract fun projectDao(): ProjectDao
    abstract fun recordDao(): RecordDao

    companion object {
        var instance: MyDataBase? = null
    }
}