package su.usatu.navigator.data.repository

import su.usatu.navigator.data.dao.HistoryDao
import su.usatu.navigator.data.entity.QueryEntity
import su.usatu.navigator.models.QueryModel
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(private val dao: HistoryDao) : HistoryRepository {
    override fun getAllHistory(): List<QueryModel> {
        val queryModels = arrayListOf<QueryModel>()

        val queryEntities = arrayListOf<QueryEntity>()
        queryEntities.addAll(dao.getAllHistory())
        queryEntities.forEach {
            queryModels.add(
                QueryModel(
                    from = it.from,
                    to = it.to
                )
            )
        }

        return queryModels
    }

    override fun addQuery(item: QueryModel) {
        item.let {
            dao.addQuery(
                QueryEntity(
                    from = it.from,
                    to = it.to
                )
            )
        }
    }

    override fun deleteAllHistory() {
        dao.deleteAllHistory()
    }

    override fun getHistorySize() =
        dao.getHistorySize()
}