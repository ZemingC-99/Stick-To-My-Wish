package com.example.StickToMyWish.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class Project(
    @PrimaryKey() @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "tab") val tab: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "start") val start: String,
    @ColumnInfo(name = "end") val end: String
)