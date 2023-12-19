package com.example.StickToMyWish.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tab")
data class Tab(
    @PrimaryKey() @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "name") val name: String,
)