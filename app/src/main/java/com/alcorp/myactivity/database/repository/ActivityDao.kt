package com.alcorp.myactivity.database.repository

import androidx.lifecycle.LiveData
import androidx.room.*

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
    fun getAllActivity(): LiveData<List<ActivityEntity>>

    @Query("SELECT * from activity_entity WHERE isDone = 1 ORDER BY timeStart ASC")
    fun getAllDoneActivity(): LiveData<List<ActivityEntity>>

    @Query("SELECT COUNT(*) from activity_entity")
    fun getCountAllActivity(): LiveData<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date = strftime('%d/%m/%Y', 'now', 'localtime')")
    fun getCountActivityToday(): LiveData<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date = strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 1")
    fun getCountCompletedActivityToday(): LiveData<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date = strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 0")
    fun getCountUncompletedActivityToday(): LiveData<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date > strftime('%d/%m/%Y', 'now', 'localtime')")
    fun getCountUpcomingActivity(): LiveData<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date > strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 1")
    fun getCountCompletedUpcomingActivity(): LiveData<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date > strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 0")
    fun getCountUncompletedUpcomingActivity(): LiveData<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE date < strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 0")
    fun getCountLateActivity(): LiveData<Int>

    @Query("SELECT COUNT(*) from activity_entity WHERE isDone = 1")
    fun getCountCompletedActivity(): LiveData<Int>

    @Query("SELECT * from activity_entity WHERE date = strftime('%d/%m/%Y', 'now', 'localtime') ORDER BY timeStart ASC")
    fun getActivityToday(): LiveData<List<ActivityEntity>>

    @Query("SELECT * from activity_entity WHERE date > strftime('%d/%m/%Y', 'now', 'localtime') ORDER BY date ASC, timeStart ASC")
    fun getUpcomingActivity(): LiveData<List<ActivityEntity>>

    @Query("SELECT * from activity_entity WHERE date < strftime('%d/%m/%Y', 'now', 'localtime') AND isDone = 0 ORDER BY date ASC, timeStart ASC")
    fun getLateActivity(): LiveData<List<ActivityEntity>>

    @Query("SELECT * from activity_entity WHERE isDone = 1 ORDER BY date ASC, timeStart ASC")
    fun getCompletedActivity(): LiveData<List<ActivityEntity>>
}