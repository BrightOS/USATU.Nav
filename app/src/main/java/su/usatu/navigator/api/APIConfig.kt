package ru.followy.util.followy_extensions.api

class APIConfig {
    companion object {
        private const val connectionType = "https"
        private const val domain = "usatunavigator.ru"
        const val getAllPointsDirectory = "points.simple"
        const val findWayDirectory = "points.find_ways"
        const val goodResponseCode = 200
        const val urlImage = "$connectionType://$domain/img/"
        const val urlAPI = "$connectionType://$domain/api/"
        const val idJsonField = "id"
        const val titleJsonField = "title"
        const val isVisibleJsonField = "is_visible"
        const val imageCodeJsonField = "img_code"
        const val fromPointIdField = "from_point_id"
        const val toPointIdField = "to_point_id"
    }
}