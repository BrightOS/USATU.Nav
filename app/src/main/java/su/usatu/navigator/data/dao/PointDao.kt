package su.usatu.navigator.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import su.usatu.navigator.data.entity.PointEntity

@Dao
interface PointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPoint(item: PointEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPoints(list: List<PointEntity>)

    @Query("SELECT * FROM points_table")
    fun getAllPoints(): List<PointEntity>

    @Query("SELECT * FROM points_table WHERE id = :id")
    fun getPointById(id: Long): List<PointEntity>

    @Delete
    fun deletePoint(item: PointEntity)

    @Query("SELECT COUNT(*) FROM points_table")
    fun getPointsSize(): LiveData<Int>
}