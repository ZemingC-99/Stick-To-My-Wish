package com.example.StickToMyWish.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class Record(
    @PrimaryKey() @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "projectId") val projectId: String,
    @ColumnInfo(name = "date") val date: String,
)
