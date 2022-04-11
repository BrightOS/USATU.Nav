package su.usatu.navigator.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_table")
data class PointEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val isVisible: Boolean,
    val imageCode: String
)
