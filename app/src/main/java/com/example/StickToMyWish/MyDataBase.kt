package com.example.StickToMyWish

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.StickToMyWish.dao.ProjectDao
import com.example.StickToMyWish.dao.RecordDao
import com.example.StickToMyWish.dao.TabDao
import com.example.StickToMyWish.entity.Project
import com.example.StickToMyWish.entity.Record
import com.example.StickToMyWish.entity.Tab
// Room Database class representing the app's database.
// It includes tables for Tabs, Projects, and Records.

// Defines the entities (tables) included in the database and sets the database version and exportSchema properties.

@Database(
    entities = [Tab::class, Project::class, Record::class], version = 1, exportSchema = false
)
abstract class MyDataBase : RoomDatabase() {

    abstract fun tabDao(): TabDao
    abstract fun projectDao(): ProjectDao
    abstract fun recordDao(): RecordDao

    // Singleton pattern to ensure only one instance of the database is created.
    companion object {
        var instance: MyDataBase? = null
    }
}