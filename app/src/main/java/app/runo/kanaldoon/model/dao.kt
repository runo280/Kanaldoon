package app.runo.kanaldoon.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChannelStore {
    @Query("SELECT * FROM ChannelEntity WHERE categoryId = :categoryId ORDER BY viewCount")
    fun getChannels(categoryId: Int): LiveData<List<ChannelEntity>>

    @Query("SELECT * FROM ChannelEntity WHERE id = :id")
    suspend fun getChannel(id: Int): ChannelEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addChannel(new: ChannelEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateChannel(new: ChannelEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateChannels(new: List<ChannelEntity>)

    @Delete
    suspend fun deleteChannel(new: ChannelEntity)
}

@Dao
interface CategoryStore {
    @Query("SELECT * FROM CategoryEntity ORDER BY `order`")
    fun getCategories(): LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM CategoryEntity WHERE id = :id")
    suspend fun getCategory(id: Int): CategoryEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addCategory(new: CategoryEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(new: CategoryEntity)

    @Delete
    suspend fun deleteCategory(new: CategoryEntity)
}