package com.example.mi_clima

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Enlazamos las nuevas variables de las tarjetas
    private lateinit var tvTemperatura: TextView
    private lateinit var tvSensacion: TextView
    private lateinit var tvHumedad: TextView
    private lateinit var tvViento: TextView
    private lateinit var tvPrecipitacion: TextView
    private lateinit var tvEstado: TextView
    private lateinit var btnActualizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos las vistas con los nuevos IDs del XML
        tvTemperatura = findViewById(R.id.tv_temperatura)
        tvSensacion = findViewById(R.id.tv_sensacion)
        tvHumedad = findViewById(R.id.tv_humedad)
        tvViento = findViewById(R.id.tv_viento)
        tvPrecipitacion = findViewById(R.id.tv_precipitacion)
        tvEstado = findViewById(R.id.tv_estado)
        btnActualizar = findViewById(R.id.btnActualizar)

        // Primera llamada al abrir la app
        obtenerClima()

        // Llamada al presionar el botón
        btnActualizar.setOnClickListener {
            obtenerClima()
        }
    }

    private fun obtenerClima() {
        lifecycleScope.launch {
            try {
                // Tu misma petición a la API
                val respuesta = RetrofitClient.api.obtenerClima(
                    latitude = -8.3791,
                    longitude = -74.5539,
                    current = "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m",
                    timezone = "America/Lima"
                )

                val clima = respuesta.current

                // Asignamos los datos a cada tarjeta, agregando la unidad de medida
                tvTemperatura.text = "${clima.temperature_2m}°C"
                tvSensacion.text = "${clima.apparent_temperature}°C"
                tvHumedad.text = "${clima.relative_humidity_2m}%"
                tvViento.text = "${clima.wind_speed_10m} km/h"
                tvPrecipitacion.text = "${clima.precipitation} mm"
                tvEstado.text = "${clima.weather_code}"

            } catch (e: Exception) {
                // En caso de error, mostramos un pequeño aviso en pantalla (Toast)
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                tvEstado.text = "Error"
            }
        }
    }
}