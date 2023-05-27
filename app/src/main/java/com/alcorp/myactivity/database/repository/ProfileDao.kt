package com.alcorp.myactivity.database.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile")
    fun getProfile() : LiveData<ProfileEntity>

    @Update
    fun update(profileData: ProfileEntity)

    @Query("UPDATE profile SET photo = :image WHERE id = :id")
    fun saveImageById(id: Int, image: Int)
}