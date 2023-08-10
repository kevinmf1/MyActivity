package com.alcorp.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alcorp.core.data.source.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(activity: ActivityEntity)

    @Update
    fun update(activity: ActivityEntity)

    @Delete
    fun delete(activity: ActivityEntity)

    @Query("DELETE FROM activity_entity WHERE id = :activityId")
    fun deleteById(activityId: Int)

    @Query("SELECT * from activity_entity WHERE isDone = 0 ORDER BY timeStart ASC")
    fun getAllActivity(): Flow<List<ActivityEntity>>

    @Query("SELECT * from activity_entity WHERE isDone = 1 ORDER BY timeStart ASC")
    fun getAllDoneActivity(): Flow<List<ActivityEntity>>

    @Query("SELECT COUNT(*) from activity_entity")
    fun getCountAllActivity(): Flow<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date = strftime('%d/%m/%Y', 'now', 'localtime')")
    fun getCountActivityToday(): Flow<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date = strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 1")
    fun getCountCompletedActivityToday(): Flow<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date = strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 0")
    fun getCountUncompletedActivityToday(): Flow<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date > strftime('%d/%m/%Y', 'now', 'localtime')")
    fun getCountUpcomingActivity(): Flow<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date > strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 1")
    fun getCountCompletedUpcomingActivity(): Flow<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date > strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 0")
    fun getCountUncompletedUpcomingActivity(): Flow<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date < strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 0")
    fun getCountLateActivity(): Flow<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE isDone = 1")
    fun getCountCompletedActivity(): Flow<Int>

    @Query("SELECT * from activity_entity WHERE date = strftime('%d/%m/%Y', 'now', 'localtime') ORDER BY timeStart ASC")
    fun getActivityToday(): Flow<List<ActivityEntity>>

    @Query("SELECT * from activity_entity WHERE date > strftime('%d/%m/%Y', 'now', 'localtime') ORDER BY date ASC, timeStart ASC")
    fun getUpcomingActivity(): Flow<List<ActivityEntity>>

    @Query("SELECT * from activity_entity WHERE date < strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 0 ORDER BY date ASC, timeStart ASC")
    fun getLateActivity(): Flow<List<ActivityEntity>>

    @Query("SELECT * from activity_entity WHERE isDone = 1 ORDER BY date ASC, timeStart ASC")
    fun getCompletedActivity(): Flow<List<ActivityEntity>>
}