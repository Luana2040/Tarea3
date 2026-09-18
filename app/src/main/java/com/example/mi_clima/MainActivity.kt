package com.example.mi_clima

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

// Modelo de datos para las ciudades
data class Ciudad(val nombre: String, val latitud: Double, val longitud: Double)

class MainActivity : AppCompatActivity() {

    private lateinit var spCiudades: Spinner
    private lateinit var tvTemperatura: TextView
    private lateinit var tvSensacion: TextView
    private lateinit var tvHumedad: TextView
    private lateinit var tvViento: TextView
    private lateinit var tvPrecipitacion: TextView
    private lateinit var tvEstado: TextView
    private lateinit var btnActualizar: Button

    // Lista de ciudades con sus coordenadas
    private val listaCiudades = listOf(
        Ciudad("Pucallpa", -8.3791, -74.5539),
        Ciudad("Lima", -12.0432, -77.0282),
        Ciudad("Ica", -14.0678, -75.7286),
        Ciudad("Arequipa", -16.4090, -71.5375),
        Ciudad("Cusco", -13.5226, -71.9673)
    )

    private var ciudadActual: Ciudad = listaCiudades[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        spCiudades = findViewById(R.id.spCiudades)
        tvTemperatura = findViewById(R.id.tv_temperatura)
        tvSensacion = findViewById(R.id.tv_sensacion)
        tvHumedad = findViewById(R.id.tv_humedad)
        tvViento = findViewById(R.id.tv_viento)
        tvPrecipitacion = findViewById(R.id.tv_precipitacion)
        tvEstado = findViewById(R.id.tv_estado)
        btnActualizar = findViewById(R.id.btnActualizar)

        // Configurar el Spinner (Menú desplegable)
        val nombresCiudades = listaCiudades.map { "📍 ${it.nombre}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresCiudades)
        spCiudades.adapter = adapter

        // Detectar cuando el usuario cambia de ciudad
        spCiudades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ciudadActual = listaCiudades[position]
                obtenerClima(ciudadActual.latitud, ciudadActual.longitud)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Botón actualizar
        btnActualizar.setOnClickListener {
            obtenerClima(ciudadActual.latitud, ciudadActual.longitud)
        }
    }

    // Función para obtener los datos de la API
    private fun obtenerClima(lat: Double, lon: Double) {
        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.api.obtenerClima(
                    latitude = lat,
                    longitude = lon,
                    current = "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m",
                    timezone = "America/Lima"
                )

                val clima = respuesta.current

                tvTemperatura.text = "${clima.temperature_2m}°C"
                tvSensacion.text = "${clima.apparent_temperature}°C"
                tvHumedad.text = "${clima.relative_humidity_2m}%"
                tvViento.text = "${clima.wind_speed_10m} km/h"
                tvPrecipitacion.text = "${clima.precipitation} mm"
                tvEstado.text = "${clima.weather_code}"

            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                tvEstado.text = "Error"
            }
        }
    }
}