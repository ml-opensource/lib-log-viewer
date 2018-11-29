package com.nodesagency.logviewer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nodesagency.logviewer.data.database.daos.CategoryDao
import com.nodesagency.logviewer.data.database.entities.Category

@Database(entities = [Category::class], version = 1)
abstract class LogDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
}