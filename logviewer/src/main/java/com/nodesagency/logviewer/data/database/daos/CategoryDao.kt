package com.nodesagency.logviewer.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nodesagency.logviewer.data.database.entities.Category

@Dao
internal interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category)

    @Query("SELECT * FROM Category ORDER BY name ASC")
    fun getAllAlphabeticallySorted(): List<Category>

}