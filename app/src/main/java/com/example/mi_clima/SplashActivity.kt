package com.example.mi_clima

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fondoCielo = findViewById<ImageView>(R.id.fondoCielo)
        val contenido = findViewById<LinearLayout>(R.id.contenido)

        // 1) Fade-in del texto central
        contenido.animate()
            .alpha(1f)
            .setDuration(1600)
            .setStartDelay(400)
            .start()

        // 2) Movimiento horizontal del fondo: las nubes se desplazan
        val moverFondo = ObjectAnimator.ofFloat(
            fondoCielo,
            "translationX",
            -300f,
            300f
        ).apply {
            duration = 12000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        moverFondo.start()

        // 3) Zoom lento para dar sensación de profundidad
        val zoomX = ObjectAnimator.ofFloat(fondoCielo, "scaleX", 1f, 1.08f).apply {
            duration = 12000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        val zoomY = ObjectAnimator.ofFloat(fondoCielo, "scaleY", 1f, 1.08f).apply {
            duration = 12000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        zoomX.start()
        zoomY.start()

        // 4) Pasar a MainActivity después de 5 s
        fondoCielo.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000)
    }
}