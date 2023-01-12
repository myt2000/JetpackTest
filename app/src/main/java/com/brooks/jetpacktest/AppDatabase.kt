package com.brooks.jetpacktest

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(version = 3, entities = [User::class, Book::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun bookDao(): BookDao

    // 类似于java中static静态变量
    companion object {
        val MIGRATION_2_3 = object: Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table Book add column author text not null default 'unknown'")
            }
        }


        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
//                .allowMainThreadQueries()
//                .fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_2_3)
                .build().apply { instance = this }

        }
    }
}