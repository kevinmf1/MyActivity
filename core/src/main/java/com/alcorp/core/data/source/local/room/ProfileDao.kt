package com.alcorp.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.alcorp.core.data.source.local.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile")
    fun getProfile() : Flow<ProfileEntity>

    @Update
    fun update(profileData: ProfileEntity)

    @Query("UPDATE profile SET photo = :image WHERE id = :id")
    fun saveImageById(id: Int, image: Int)
}