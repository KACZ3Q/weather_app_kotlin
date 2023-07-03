package com.example.weatherappv1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weatherappv1.network.ApiService
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: ConstraintLayout
    private lateinit var editTextCity: EditText
    private lateinit var imageButtonSearch: ImageButton
    private lateinit var textViewTemperature: TextView
    private lateinit var textViewHumidity: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewPressure: TextView
    private lateinit var textViewFeel: TextView
    private lateinit var minTemp: TextView
    private lateinit var maxTemp: TextView
    private lateinit var apiService: ApiService
    private lateinit var textViewLocation: TextView

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = ApiService()

        textViewLocation=findViewById(R.id.textViewLocation)
        editTextCity = findViewById(R.id.editTextCity)
        imageButtonSearch = findViewById(R.id.imageButtonSearch)
        textViewTemperature = findViewById(R.id.textViewTemperature)
        textViewHumidity = findViewById(R.id.textViewHumidity)
        textViewDescription = findViewById(R.id.textViewDescription)
        textViewPressure = findViewById(R.id.textViewPressure)
        textViewFeel = findViewById(R.id.textViewFeel)
        minTemp = findViewById(R.id.minTemp)
        maxTemp = findViewById(R.id.maxTemp)
        mainLayout = findViewById(R.id.mainLayout)

        imageButtonSearch.setOnClickListener {
            val cityName = editTextCity.text.toString()
            if (cityName.isNotEmpty()) {
                searchWeatherData(cityName)
            } else {
                Toast.makeText(this, "Type City name again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchWeatherData(cityName: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val weatherData = withContext(Dispatchers.IO) { apiService.getWeatherData("6644b3a309b45619eff765a5835e8e7a", cityName) }
                weatherData?.let {
                    val mainObject = it.getJSONObject("main")
                    val weatherArray = it.getJSONArray("weather")
                    val temperature = mainObject.getDouble("temp").toInt()
                    val humidity = mainObject.getInt("humidity")
                    val feelLike = mainObject.getInt("feels_like")
                    val weatherDescription = weatherArray.getJSONObject(0).getString("main")
                    val pressure = mainObject.getInt("pressure")
                    val tempMin = mainObject.getDouble("temp_min").toInt()
                    val tempMax = mainObject.getDouble("temp_max").toInt()
                    val location = it.getString("name")

                    when (weatherDescription) {
                        "Rain" -> mainLayout.setBackgroundResource(R.drawable.rain)
                        "Clear" -> mainLayout.setBackgroundResource(R.drawable.clear)
                        "Clouds" -> mainLayout.setBackgroundResource(R.drawable.clouds)
                        "Snow" -> mainLayout.setBackgroundResource(R.drawable.snow)
                        "Thunderstorm" -> mainLayout.setBackgroundResource(R.drawable.storm)
                        "Haze" -> mainLayout.setBackgroundResource(R.drawable.haze)
                        "Mist" -> mainLayout.setBackgroundResource(R.drawable.haze)
                        "Fog" -> mainLayout.setBackgroundResource(R.drawable.haze)
                    }

                    textViewTemperature.text = "$temperature°C"
                    textViewHumidity.text = "$humidity%"
                    textViewDescription.text = weatherDescription
                    textViewPressure.text = "$pressure hpa"
                    textViewFeel.text = "$feelLike°C"
                    minTemp.text = "$tempMin°C"
                    maxTemp.text = "$tempMax°C"
                    textViewLocation.text = location
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

