package com.nodesagency.logviewer.data.database.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nodesagency.logviewer.data.model.Category

@Dao
internal interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category): Long

    @Query("SELECT * FROM Categories ORDER BY name ASC")
    fun getAlphabeticallySortedCategories(): DataSource.Factory<Int, Category>

    @Query("SELECT * FROM Categories WHERE name = :name LIMIT 1")
    fun getCategoryWithName(name: String): Category?

    @Query("SELECT * FROM Categories LIMIT :limit OFFSET :skip")
    fun getAlphabeticallySorted(skip: Long, limit: Long): List<Category>

    @Query("DELETE FROM Categories")
    fun deleteAllCategories()

}