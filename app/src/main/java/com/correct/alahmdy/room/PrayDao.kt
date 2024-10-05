package com.correct.alahmdy.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.correct.alahmdy.data.user.PrayTime


@Dao
interface PrayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: PrayTime)

    @Query("SELECT * FROM pray")
    suspend fun getAll(): List<PrayTime>

    @Query("SELECT * FROM pray WHERE id = :id")
    suspend fun getById(id: Int): PrayTime

    @Query("select * from pray where user_id = :userID")
    suspend fun getByUserID(userID: Int): List<PrayTime>?

    @Query("delete from pray where 1")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(entity: PrayTime)

}