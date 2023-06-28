package com.example.weatherappv1.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class ApiService {
    private val client = OkHttpClient()

    fun getWeatherData(apiKey: String, city: String): JSONObject? {

        val url = "http://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"
        val request = Request.Builder().url(url).build()
        val response: Response = client.newCall(request).execute()
        val responseData = response.body?.string()
        return responseData?.let { JSONObject(it) }
    }
}