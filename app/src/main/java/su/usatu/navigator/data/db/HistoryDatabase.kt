package su.usatu.navigator.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import su.usatu.navigator.data.dao.HistoryDao
import su.usatu.navigator.data.entity.QueryEntity

@Database(entities = arrayOf(QueryEntity::class), version = 3, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}