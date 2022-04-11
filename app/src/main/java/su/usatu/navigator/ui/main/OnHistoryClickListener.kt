package su.usatu.navigator.ui.main

import su.usatu.navigator.models.QueryModel

interface OnHistoryClickListener {
    fun onHistoryClick(item: QueryModel, position: Long)
}