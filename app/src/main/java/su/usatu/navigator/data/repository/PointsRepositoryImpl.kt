package su.usatu.navigator.data.repository

import androidx.lifecycle.LiveData
import su.usatu.navigator.data.dao.PointDao
import su.usatu.navigator.data.entity.PointEntity
import su.usatu.navigator.models.PointModel
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(private val dao: PointDao) : PointsRepository {

    override fun getAllPoints(): List<PointModel> {
        val pointsList = arrayListOf<PointModel>()

        val pointsEntities = arrayListOf<PointEntity>()
        pointsEntities.addAll(dao.getAllPoints())
        pointsEntities.sortBy { it.title }
        pointsEntities.forEach {
            pointsList.add(
                PointModel(
                    id = it.id,
                    title = it.title,
                    isVisible = it.isVisible,
                    imageCode = it.imageCode
                )
            )
        }

        return pointsList
    }

    override fun addPoint(item: PointModel) {
        item.let {
            dao.addPoint(
                PointEntity(
                    id = it.id,
                    title = it.title,
                    isVisible = it.isVisible,
                    imageCode = it.imageCode
                )
            )
        }
    }

    override fun addPoints(list: List<PointModel>) {
        val pointsEntities = arrayListOf<PointEntity>()
        list.forEach {
            pointsEntities.add(
                PointEntity(
                    id = it.id,
                    title = it.title,
                    isVisible = it.isVisible,
                    imageCode = it.imageCode
                )
            )
        }
        dao.deleteAllPoints()
        dao.addPoints(pointsEntities)
    }

    override fun getPointById(id: Long): PointModel? =
        dao.getPointById(id).let {
            if (it.size > 0)
                it[0].let {
                    PointModel(
                        id = it.id,
                        title = it.title,
                        isVisible = it.isVisible,
                        imageCode = it.imageCode
                    )
                }
            else
                null
        }

    override fun deletePoint(id: Long) {
        dao.getPointById(id).let {
            if (it.size > 0)
                dao.deletePoint(it[0])
        }
    }

    override fun getPointsSize(): LiveData<Int> = dao.getPointsSize()
}