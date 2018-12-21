package com.nodesagency.logviewer.data.database.daos

import androidx.paging.DataSource
import androidx.room.*
import com.nodesagency.logviewer.data.model.Category

@Dao
internal interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category): Long

    @Query("SELECT * FROM Categories ORDER BY isPinned DESC, name ASC")
    fun getAlphabeticallySortedCategories(): DataSource.Factory<Int, Category>

    @Query("SELECT * FROM Categories WHERE name = :name LIMIT 1")
    fun getCategoryWithName(name: String): Category?

    @Query("SELECT * FROM Categories LIMIT :limit OFFSET :skip")
    fun getAlphabeticallySorted(skip: Long, limit: Long): List<Category>

    @Query("DELETE FROM Categories")
    fun deleteAllCategories()


    @Query("UPDATE Categories SET isPinned = :isPinned WHERE id = :categoryId")
    fun updatePinnedValue(categoryId: Long, isPinned: Boolean)

}