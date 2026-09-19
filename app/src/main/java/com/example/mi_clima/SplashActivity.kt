package com.example.mi_clima

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val nube1 = findViewById<LinearLayout>(R.id.nube1)
        val nube2 = findViewById<LinearLayout>(R.id.nube2)
        val nube3 = findViewById<LinearLayout>(R.id.nube3)
        val contenido = findViewById<LinearLayout>(R.id.contenido)

        // Fade-in del contenido central
        contenido.animate()
            .alpha(1f)
            .setDuration(1500)
            .setStartDelay(300)
            .start()

        // Animaciones de nubes: desplazamiento + vaivén vertical (más realista)
        animarNube(nube1, -400f, 1100f, 9000, -25f)
        animarNube(nube2, 1100f, -500f, 11000, 30f)
        animarNube(nube3, -600f, 1200f, 8000, -18f)

        // Pasar a MainActivity después de 5 s
        nube1.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000)
    }

    /**
     * Anima una nube horizontalmente y le da un pequeño vaivén vertical
     * para simular flotación.
     */
    private fun animarNube(
        vista: LinearLayout,
        desdeX: Float,
        hastaX: Float,
        duracion: Long,
        amplitudY: Float
    ) {
        val moverX = ObjectAnimator.ofFloat(vista, "translationX", desdeX, hastaX).apply {
            this.duration = duracion
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }

        val flotarY = ObjectAnimator.ofFloat(vista, "translationY", 0f, amplitudY, 0f).apply {
            this.duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        AnimatorSet().apply {
            playTogether(moverX, flotarY)
            start()
        }
    }
}