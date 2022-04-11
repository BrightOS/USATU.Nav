package su.usatu.navigator.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import su.usatu.navigator.data.entity.QueryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuery(item: QueryEntity)

    @Query("SELECT * FROM history_table")
    fun getAllHistory(): List<QueryEntity>

    @Delete
    fun deleteQuery(item: QueryEntity)

    @Query("DELETE FROM history_table")
    fun deleteAllHistory()

    @Query("SELECT COUNT(*) FROM history_table")
    fun getHistorySize(): LiveData<Int>
}