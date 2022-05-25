package ru.followy.util.followy_extensions.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.TimeUnit


class APIUtils {
    companion object {

        /**
         * This is a method to send a Post request using JSON or "x-www-form-urlencoded" schemas
         * @param url = url link
         * @param jsonRequestBody = a JSON object that contains all the required arguments
         * @param auth = Bearer token. May be null
         * @param contentType = request schema type
         */
        internal fun post(
            url: String,
            jsonRequestBody: JSONObject,
            auth: String?,
            contentType: String
        ): USATUNavigatorResponse {
            val request =
                preparePostRequest(url, jsonRequestBody, auth, contentType)
            val response = request!!.let { sendRequest(request) }

            val code = response.code
            val body = response.body?.string()

            response.close()

            return USATUNavigatorResponse(code, body!!)
        }

        /**
         * This is a method to send a Get request to the server using a URL and a Bearer token
         * @param url = url link with all Query arguments
         * @param auth = Bearer token
         */
        internal fun get(
            url: String,
            auth: String?
        ): USATUNavigatorResponse {

            val request = prepareGetRequest(url, auth)
            val response = sendRequest(request)

            val code = response.code
            val body = response.body?.string()

            response.close()

            return USATUNavigatorResponse(code, body!!)
        }

        /**
         * This is a method to send a Delete request to the server using a URL and a Bearer token
         * @param url = url link with all Query arguments
         * @param auth = Bearer token
         */
        internal fun delete(
            url: String,
            auth: String
        ): USATUNavigatorResponse {

            val request = prepareDeleteRequest(url, auth)
            val response = sendRequest(request)

            val code = response.code
            val body = response.body?.string()

            response.close()

            return USATUNavigatorResponse(code, body!!)
        }

        /**
         * This is a method to send a Put request to the server using a URL and a Bearer token
         * @param url = url link with all Query arguments
         * @param auth = Bearer token
         */
        internal fun put(
            url: String,
            jsonRequestBody: JSONObject,
            auth: String
        ): USATUNavigatorResponse {
            val request = preparePutRequest(url, jsonRequestBody, auth)
            val response = sendRequest(request)

            val code = response.code
            val body = response.body?.string()

            response.close()

            return USATUNavigatorResponse(code, body!!)
        }

        /**
         * This is a method to download image by name
         * @param imageFileName = image file name on server
         * @return Bitmap of the requested image
         */
        internal fun downloadImage(imageFileName: String): Bitmap? {
            val url = URL(("${APIConfig.urlImage}$imageFileName"))
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }

        private fun jsonToRequestBody(jsonObject: JSONObject) =
            "$jsonObject".toRequestBody("application/json; charset=utf-8".toMediaType())

        private fun jsonToFormRequestBody(jsonObject: JSONObject): FormBody {
            val requestBody = FormBody.Builder()
            val temp: Iterator<String> = jsonObject.keys()
            var key: String
            while (temp.hasNext()) {
                key = temp.next()
                val value: Any = jsonObject.get(key)
                requestBody.add(key, value as String)
            }
            return requestBody.build()
        }

        private fun preparePostRequest(
            url: String,
            jsonObject: JSONObject,
            auth: String?,
            contentType: String
        ): Request? {
            when (contentType) {
                JSON ->
                    if (auth == null)
                        return Request.Builder()
                            .url("${APIConfig.urlAPI}$url")
                            .header("apikey", "123456789")
                            .post(jsonToRequestBody(jsonObject))
                            .build()
                    else
                        return Request.Builder()
                            .url("${APIConfig.urlAPI}$url")
                            .header("apikey", "123456789")
                            .addHeader("Authorization", "Bearer $auth")
                            .post(jsonToRequestBody(jsonObject))
                            .build()

                URL_ENCODED -> {
                    if (auth == null)
                        return Request.Builder()
                            .url("${APIConfig.urlAPI}$url")
                            .header("apikey", "123456789")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .post(jsonToFormRequestBody(jsonObject))
                            .build()
                    else
                        return Request.Builder()
                            .url("${APIConfig.urlAPI}$url")
                            .header("apikey", "123456789")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", "Bearer $auth")
                            .post(jsonToFormRequestBody(jsonObject))
                            .build()
                }

                else -> return null
            }
        }

        private fun prepareGetRequest(url: String, auth: String?) =
            if (auth != null)
                Request.Builder()
                    .url("${APIConfig.urlAPI}$url")
                    .addHeader("Authorization", "Bearer $auth")
                    .header("apikey", "123456789")
                    .addHeader("charset", "utf-8")
                    .get()
                    .build()
            else
                Request.Builder()
                    .url("${APIConfig.urlAPI}$url")
                    .header("apikey", "123456789")
                    .addHeader("charset", "utf-8")
                    .get()
                    .build()

        private fun prepareDeleteRequest(url: String, auth: String) =
            Request.Builder()
                .url("${APIConfig.urlAPI}$url")
                .addHeader("Authorization", "Bearer $auth")
                .header("apikey", "123456789")
                .addHeader("charset", "utf-8")
                .delete()
                .build()

        private fun preparePutRequest(url: String, jsonObject: JSONObject, auth: String) =
            Request.Builder()
                .url("${APIConfig.urlAPI}$url")
                .header("apikey", "123456789")
                .addHeader("Authorization", "Bearer $auth")
                .put(jsonToRequestBody(jsonObject))
                .build()

        private fun sendRequest(request: Request) = OkHttpClient
            .Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .callTimeout(2, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()
            .newCall(request)
            .execute()

        const val JSON = "application/json"
        const val URL_ENCODED = "application/x-www-form-urlencoded"
    }
}