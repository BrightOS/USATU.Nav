package su.usatu.navigator.models

data class QueryModel(
    val from: String,
    val to: String,
    val pointsList: List<PointModel>
)
