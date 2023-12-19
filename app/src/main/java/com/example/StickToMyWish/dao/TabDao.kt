package com.example.StickToMyWish.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.StickToMyWish.entity.Tab

@Dao
interface TabDao : BaseDao<Tab> {

    @Query("select * from tab")
    fun getTabList(): List<Tab>

}