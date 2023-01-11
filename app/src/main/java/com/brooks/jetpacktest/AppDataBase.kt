package com.brooks.jetpacktest

import android.content.Context
import androidx.room.*


@Database(version = 1, entities = [User::class])
abstract class AppDataBase: RoomDatabase() {

    abstract fun userDao(): UserDao

    // 类似于java中static静态变量
    companion object {
        private var instance: AppDataBase? = null
    }

    fun getDatabase(context: Context): AppDataBase {
        instance?.let {
            return it
        }
        return Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "app_database")
            .build().apply { instance = this }
    }
}