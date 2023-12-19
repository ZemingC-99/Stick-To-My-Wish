package com.example.StickToMyWish.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.StickToMyWish.entity.Record

@Dao
interface RecordDao : BaseDao<Record> {

    @Query("select * from record where projectId = :id")
    fun getRecordList(id: String): List<Record>

}