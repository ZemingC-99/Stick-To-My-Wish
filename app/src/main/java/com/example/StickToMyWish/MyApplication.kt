package com.example.StickToMyWish

import android.app.Application
import android.content.SharedPreferences.Editor
import androidx.room.Room
import com.example.StickToMyWish.MyDataBase.Companion.instance
import com.example.StickToMyWish.entity.Project


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this;
        instance = Room.databaseBuilder(this, MyDataBase::class.java, "database").build()
        val sps = getSharedPreferences("share", MODE_PRIVATE)
        passwordAble = sps.getBoolean("passwordAble",false)
        notificationAble = sps.getBoolean("notificationAble",false)
        darkMode = sps.getBoolean("darkMode",false)
        notification = sps.getString("notification","never")!!
        shareDefault = sps.getString("shareDefault",null)
        password = sps.getString("password",null)
    }

    companion object {

        lateinit var application: MyApplication;
        lateinit var projectList: List<Project>
        var passwordAble : Boolean = false
        var notificationAble : Boolean = false
        lateinit var notification : String
        var password : String? = null
        var shareDefault : String? = null
        var darkMode : Boolean = false
        @JvmStatic var projectIndex : Int = -1

        fun getSharedPreferencesEditor () : Editor{
            return application.getSharedPreferences("share", MODE_PRIVATE).edit()
        }

    }

}