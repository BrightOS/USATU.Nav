package ru.glueme.api

import android.graphics.Bitmap
import org.json.JSONArray
import ru.followy.util.followy_extensions.api.APIConfig
import ru.followy.util.followy_extensions.api.APIUtils
import ru.followy.util.followy_extensions.api.USATUNavigatorResponse
import su.usatu.navigator.api.BadConnectionException
import su.usatu.navigator.models.PointModel

object USATUNavigatorAPI {

    private fun stringJsonArrayToPointsList(jsonArrayString: String): ArrayList<PointModel> {
        val pointsList: ArrayList<PointModel> = ArrayList()
        val jsonArray = JSONArray(jsonArrayString)
        for (i in 0 until jsonArray.length()) {
            val jsonPoint = jsonArray.getJSONObject(i)
            val newPoint = PointModel(
                jsonPoint.getInt(APIConfig.idJsonField),
                jsonPoint.getString(APIConfig.titleJsonField),
                jsonPoint.getBoolean(APIConfig.isVisibleJsonField),
                jsonPoint.getString(APIConfig.imageCodeJsonField)
            )
            pointsList.add(newPoint)
        }
        return pointsList
    }

    private fun sendSafeGetRequestToAPI(url: String): USATUNavigatorResponse {
        try {
            val response = APIUtils.get(url, null)
            if (response.code != APIConfig.goodResponseCode) {
                throw BadConnectionException()
            }
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            throw BadConnectionException()
        }
    }

    /**
     * This is a method to get all points from server
     * @return list of all points
     * @throws BadConnectionException if get request failed or request is invalid or response code is bad
     */
    fun getAllPoints(): ArrayList<PointModel> {
        val response = sendSafeGetRequestToAPI(APIConfig.getAllPointsDirectory)
        return stringJsonArrayToPointsList(response.body)
    }

    /**
     * This is a method to find way from one point to another
     * @param fromPointId = start point of the route
     * @param toPointId = end point of the route
     * @return list of control points of the route
     * @throws BadConnectionException if get request failed or response code is bad
     */
    fun findWayByPointsId(fromPointId: Int, toPointId: Int): ArrayList<PointModel> {
        val urlWithParameters =
            "${APIConfig.findWayDirectory}?${APIConfig.fromPointIdField}=${fromPointId}&${APIConfig.toPointIdField}=${toPointId}"
        val response = sendSafeGetRequestToAPI(urlWithParameters)
        return stringJsonArrayToPointsList(response.body)
    }

    /**
     * This is a method to download image from the server by image code
     * @param imageCode = start point of the route
     * @return Bitmap of requested image
     * @throws BadConnectionException if downloading Bitmap failed
     */
    fun getImageByCode(imageCode: String): Bitmap? {
        try {
            return APIUtils.downloadImage(imageCode)
        } catch (e: Exception) {
            throw BadConnectionException()
        }
    }
}