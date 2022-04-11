package su.usatu.navigator.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "history_table", indices = [Index(value = ["from", "to"], unique = true)])
data class QueryEntity(
    @ColumnInfo(name = "from")
    val from: String,
    @ColumnInfo(name = "to")
    val to: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
