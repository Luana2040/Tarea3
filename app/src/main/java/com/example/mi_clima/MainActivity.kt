package com.example.mi_clima

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TextViews de las 5 ciudades fijas
        val tvPucallpa = findViewById<TextView>(R.id.tvTempPucallpa)
        val tvLima = findViewById<TextView>(R.id.tvTempLima)
        val tvIca = findViewById<TextView>(R.id.tvTempIca)
        val tvArequipa = findViewById<TextView>(R.id.tvTempArequipa)
        val tvCusco = findViewById<TextView>(R.id.tvTempCusco)

        // Tarjetas de las 5 ciudades fijas
        val cardPucallpa = findViewById<MaterialCardView>(R.id.cardPucallpa)
        val cardLima = findViewById<MaterialCardView>(R.id.cardLima)
        val cardIca = findViewById<MaterialCardView>(R.id.cardIca)
        val cardArequipa = findViewById<MaterialCardView>(R.id.cardArequipa)
        val cardCusco = findViewById<MaterialCardView>(R.id.cardCusco)

        // Tarjeta extra de búsqueda
        val cardBusqueda = findViewById<MaterialCardView>(R.id.cardBusqueda)
        val tvNombreBusqueda = findViewById<TextView>(R.id.tvNombreBusqueda)
        val tvPaisBusqueda = findViewById<TextView>(R.id.tvPaisBusqueda)
        val tvTempBusqueda = findViewById<TextView>(R.id.tvTempBusqueda)

        // Buscador
        val etBuscar = findViewById<EditText>(R.id.etBuscar)

        // Cargar clima de las 5 ciudades fijas
        obtenerClima(-8.3791, -74.5539, tvPucallpa)
        obtenerClima(-12.0432, -77.0282, tvLima)
        obtenerClima(-14.0678, -75.7286, tvIca)
        obtenerClima(-16.4090, -71.5375, tvArequipa)
        obtenerClima(-13.5226, -71.9673, tvCusco)

        // Lista de ciudades fijas
        val ciudades = listOf(
            cardPucallpa to "pucallpa peru",
            cardLima to "lima peru",
            cardIca to "ica peru",
            cardArequipa to "arequipa peru",
            cardCusco to "cusco cuzco peru"
        )

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().trim().lowercase()

                // 1) Filtrar las 5 tarjetas fijas
                var algunaCoincide = false
                ciudades.forEach { (card, claves) ->
                    val coincide = texto.isEmpty() || claves.contains(texto)
                    card.visibility = if (coincide) View.VISIBLE else View.GONE
                    if (coincide && texto.isNotEmpty()) algunaCoincide = true
                }

                // 2) Decidir si mostramos la tarjeta de búsqueda externa
                when {
                    texto.isEmpty() -> {
                        // Sin texto: ocultar tarjeta extra y mostrar todo
                        cardBusqueda.visibility = View.GONE
                        ciudades.forEach { (card, _) -> card.visibility = View.VISIBLE }
                    }
                    algunaCoincide -> {
                        // Coincide con una fija: ocultar tarjeta extra
                        cardBusqueda.visibility = View.GONE
                    }
                    texto.length >= 3 -> {
                        // No coincide con ninguna fija y tiene >=3 letras: buscar en API
                        cardBusqueda.visibility = View.VISIBLE
                        tvNombreBusqueda.text = s.toString().trim()
                        tvPaisBusqueda.text = "Buscando..."
                        tvTempBusqueda.text = "🔎 Buscando..."
                        buscarCiudadEnApi(texto, tvNombreBusqueda, tvPaisBusqueda, tvTempBusqueda)
                    }
                    else -> {
                        // Menos de 3 letras y no coincide: ocultar tarjeta extra
                        cardBusqueda.visibility = View.GONE
                    }
                }
            }
        })
    }

    /**
     * Geocodifica el nombre de la ciudad y consulta el clima en la API.
     */
    private fun buscarCiudadEnApi(
        nombre: String,
        tvNombre: TextView,
        tvPais: TextView,
        tvTemp: TextView
    ) {
        lifecycleScope.launch {
            try {
                // 1) Geocodificar el nombre -> lat/lon (en hilo IO)
                val direccion: Address? = withContext(Dispatchers.IO) {
                    try {
                        val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                        val resultados = geocoder.getFromLocationName(nombre, 1)
                        if (resultados.isNullOrEmpty()) null else resultados[0]
                    } catch (e: Exception) {
                        null
                    }
                }

                if (direccion == null) {
                    tvPais.text = "—"
                    tvTemp.text = "❌ No se encontró \"$nombre\""
                    return@launch
                }

                // 2) Mostrar nombre y país detectado
                val ciudad = direccion.locality
                    ?: direccion.subAdminArea
                    ?: direccion.adminArea
                    ?: nombre.replaceFirstChar { it.uppercase() }
                val pais = direccion.countryName ?: "—"

                tvNombre.text = ciudad
                tvPais.text = pais
                tvTemp.text = "🔎 Consultando clima..."

                // 3) Consultar la API con las coordenadas
                val respuesta = RetrofitClient.api.obtenerClima(
                    latitude = direccion.latitude,
                    longitude = direccion.longitude,
                    current = "temperature_2m,relative_humidity_2m,wind_speed_10m",
                    timezone = "auto"
                )

                val temp = respuesta.current.temperature_2m
                val hum = respuesta.current.relative_humidity_2m
                val viento = respuesta.current.wind_speed_10m

                tvTemp.text = "🌡️ $temp°C   |   💧 $hum%   |   💨 $viento km/h"

            } catch (e: Exception) {
                tvPais.text = "—"
                tvTemp.text = "⚠️ Error al consultar"
            }
        }
    }

    /**
     * Consulta el clima para una lat/lon y actualiza el TextView dado.
     */
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