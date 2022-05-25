package su.usatu.navigator.data.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import su.usatu.navigator.models.PointModel
import java.lang.reflect.Type

class HistoryTypeConverters {
    @TypeConverter
    fun stringToPointsList(json: String?): List<PointModel> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<PointModel>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun pointsListToString(list: List<PointModel>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<PointModel>>() {}.type
        return gson.toJson(list, type)
    }
}