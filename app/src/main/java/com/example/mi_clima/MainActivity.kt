package com.example.mi_clima

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var txtTemperatura: TextView
    private lateinit var txtHumedad: TextView
    private lateinit var txtSensacion: TextView
    private lateinit var txtViento: TextView
    private lateinit var txtPrecipitacion: TextView
    private lateinit var txtEstado: TextView
    private lateinit var btnActualizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        txtTemperatura = findViewById(R.id.txtTemperatura)
        txtHumedad = findViewById(R.id.txtHumedad)
        txtSensacion = findViewById(R.id.txtSensacion)
        txtViento = findViewById(R.id.txtViento)
        txtPrecipitacion = findViewById(R.id.txtPrecipitacion)
        txtEstado = findViewById(R.id.txtEstado)
        btnActualizar = findViewById(R.id.btnActualizar)

        obtenerClima()

        btnActualizar.setOnClickListener {
            obtenerClima()
        }
    }

    private fun obtenerClima() {

        lifecycleScope.launch {

            try {

                val respuesta = RetrofitClient.api.obtenerClima(
                    latitude = -8.3791,
                    longitude = -74.5539,
                    current = "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m",
                    timezone = "America/Lima"
                )

                val clima = respuesta.current

                txtTemperatura.text =
                    "🌡️ Temperatura: ${clima.temperature_2m} °C"

                txtHumedad.text =
                    "💧 Humedad: ${clima.relative_humidity_2m} %"

                txtSensacion.text =
                    "🌡️ Sensación térmica: ${clima.apparent_temperature} °C"

                txtViento.text =
                    "💨 Viento: ${clima.wind_speed_10m} km/h"

                txtPrecipitacion.text =
                    "🌧️ Precipitación: ${clima.precipitation} mm"

                txtEstado.text =
                    "Código del clima: ${clima.weather_code}"

            } catch (e: Exception) {

                txtEstado.text =
                    "Error al obtener el clima: ${e.message}"
            }
        }
    }
}