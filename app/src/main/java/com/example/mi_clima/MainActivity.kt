package com.example.mi_clima

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Enlazamos los TextViews de cada tarjeta
        val tvPucallpa = findViewById<TextView>(R.id.tvTempPucallpa)
        val tvLima = findViewById<TextView>(R.id.tvTempLima)
        val tvIca = findViewById<TextView>(R.id.tvTempIca)
        val tvArequipa = findViewById<TextView>(R.id.tvTempArequipa)
        val tvCusco = findViewById<TextView>(R.id.tvTempCusco)

        // 2. Pedimos el clima a la API para cada ciudad por separado
        obtenerClima(-8.3791, -74.5539, tvPucallpa)   // Pucallpa
        obtenerClima(-12.0432, -77.0282, tvLima)      // Lima
        obtenerClima(-14.0678, -75.7286, tvIca)       // Ica
        obtenerClima(-16.4090, -71.5375, tvArequipa)  // Arequipa
        obtenerClima(-13.5226, -71.9673, tvCusco)     // Cusco
    }

    // Función que consulta la API y actualiza la tarjeta específica
    private fun obtenerClima(lat: Double, lon: Double, textView: TextView) {
        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.api.obtenerClima(
                    latitude = lat,
                    longitude = lon,
                    current = "temperature_2m,relative_humidity_2m,wind_speed_10m",
                    timezone = "America/Lima"
                )

                val temp = respuesta.current.temperature_2m
                val hum = respuesta.current.relative_humidity_2m
                val viento = respuesta.current.wind_speed_10m

                textView.text = "🌡️ $temp°C   |   💧 $hum%   |   💨 $viento km/h"

            } catch (e: Exception) {
                textView.text = "Error al cargar datos"
            }
        }
    }
}