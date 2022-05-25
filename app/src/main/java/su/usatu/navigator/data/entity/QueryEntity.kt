package su.usatu.navigator.data.entity

import androidx.room.*
import su.usatu.navigator.models.PointModel

@Entity(
    tableName = "history_table",
    indices = [Index(value = ["from", "to"], unique = true), Index(value = ["way"], unique = false)]
)
@TypeConverters(HistoryTypeConverters::class)
data class QueryEntity(
    @ColumnInfo(name = "from")
    val from: String,
    @ColumnInfo(name = "to")
    val to: String,
    @ColumnInfo(name = "way")
    val pointsList: List<PointModel>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
