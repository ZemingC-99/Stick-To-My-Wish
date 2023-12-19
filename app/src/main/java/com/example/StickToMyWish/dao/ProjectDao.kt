package com.example.StickToMyWish.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.StickToMyWish.entity.Project

@Dao
interface ProjectDao : BaseDao<Project> {

    @Query("select * from project")
    fun getProjectList(): List<Project>

}