package su.usatu.navigator.data.repository

import androidx.lifecycle.LiveData
import su.usatu.navigator.models.QueryModel

interface HistoryRepository {
    fun getAllHistory(): List<QueryModel>
    fun addQuery(item: QueryModel)
    fun deleteAllHistory()
    fun getHistorySize(): LiveData<Int>
}