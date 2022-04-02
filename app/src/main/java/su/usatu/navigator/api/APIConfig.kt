package ru.followy.util.followy_extensions.api

class APIConfig {
    companion object {
        private const val connectionType = "https"
        private const val domain = "glueme.herokuapp.com"
        const val errorCode = 7012020
        const val url = "$connectionType://$domain/api/"
    }
}