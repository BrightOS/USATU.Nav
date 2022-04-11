package su.usatu.navigator.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import su.usatu.navigator.data.dao.PointDao
import su.usatu.navigator.data.entity.PointEntity

@Database(entities = arrayOf(PointEntity::class), version = 1, exportSchema = false)
abstract class PointsDatabase : RoomDatabase() {
    abstract fun pointDao(): PointDao
}