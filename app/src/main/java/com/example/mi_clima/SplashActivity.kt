package com.example.mi_clima

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fondoCielo = findViewById<ImageView>(R.id.fondoCielo)
        val contenido = findViewById<LinearLayout>(R.id.contenido)
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val tvSubtitulo = findViewById<TextView>(R.id.tvSubtitulo)

        // ===== 1) Fade-in del contenido =====
        contenido.animate()
            .alpha(1f)
            .setDuration(1600)
            .setStartDelay(400)
            .start()

        // ===== 2) Movimiento del fondo =====
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

        // ===== 3) Zoom lento del fondo =====
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

        // ===== 4) Animación de COLOR del título =====
        // Va cambiando entre blanco → celeste → dorado → blanco
        val colorAnim = ValueAnimator.ofObject(
            ArgbEvaluator(),
            0xFFFFFFFF.toInt(),  // blanco
            0xFFB3E5FC.toInt(),  // celeste claro
            0xFFFFD54F.toInt(),  // dorado
            0xFFFFFFFF.toInt()   // blanco otra vez
        ).apply {
            duration = 4000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { anim ->
                tvTitulo.setTextColor(anim.animatedValue as Int)
            }
        }
        colorAnim.start()

        // ===== 5) Brillo pulsante en el título (scaleX/scaleY) =====
        val pulsoX = ObjectAnimator.ofFloat(tvTitulo, "scaleX", 1f, 1.06f, 1f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        val pulsoY = ObjectAnimator.ofFloat(tvTitulo, "scaleY", 1f, 1.06f, 1f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        pulsoX.start()
        pulsoY.start()

        // ===== 6) Entrada del título: sube desde abajo =====
        tvTitulo.translationY = 40f
        tvTitulo.alpha = 0f
        tvTitulo.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(1200)
            .setStartDelay(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // ===== 7) Entrada del subtítulo =====
        tvSubtitulo.translationY = 30f
        tvSubtitulo.alpha = 0f
        tvSubtitulo.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(1200)
            .setStartDelay(900)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // ===== 8) Parpadeo suave del subtítulo (tipo "cargando") =====
        val parpadeo = ObjectAnimator.ofFloat(tvSubtitulo, "alpha", 1f, 0.4f, 1f).apply {
            duration = 1800
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        parpadeo.start()

        // ===== 9) Ir a MainActivity a los 5 segundos =====
        fondoCielo.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000)
    }
}