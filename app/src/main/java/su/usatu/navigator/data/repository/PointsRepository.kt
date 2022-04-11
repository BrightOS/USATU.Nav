package su.usatu.navigator.data.repository

import androidx.lifecycle.LiveData
import su.usatu.navigator.models.PointModel

interface PointsRepository {
    fun getAllPoints(): List<PointModel>
    fun addPoint(item: PointModel)
    fun addPoints(list: List<PointModel>)
    fun getPointById(id: Long): PointModel?
    fun deletePoint(id: Long)
    fun getPointsSize(): LiveData<Int>
}