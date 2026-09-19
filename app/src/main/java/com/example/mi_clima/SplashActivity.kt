package com.example.mi_clima

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val nube1 = findViewById<TextView>(R.id.nube1)
        val nube2 = findViewById<TextView>(R.id.nube2)
        val nube3 = findViewById<TextView>(R.id.nube3)

        // Animación de la nube 1
        val animacionNube1 = ObjectAnimator.ofFloat(
            nube1,
            "translationX",
            -300f,
            900f
        )

        animacionNube1.duration = 7000
        animacionNube1.repeatCount = ObjectAnimator.INFINITE
        animacionNube1.interpolator = LinearInterpolator()
        animacionNube1.start()

        // Animación de la nube 2
        val animacionNube2 = ObjectAnimator.ofFloat(
            nube2,
            "translationX",
            300f,
            -700f
        )

        animacionNube2.duration = 9000
        animacionNube2.repeatCount = ObjectAnimator.INFINITE
        animacionNube2.interpolator = LinearInterpolator()
        animacionNube2.start()

        // Animación de la nube 3
        val animacionNube3 = ObjectAnimator.ofFloat(
            nube3,
            "translationX",
            -500f,
            800f
        )

        animacionNube3.duration = 8000
        animacionNube3.repeatCount = ObjectAnimator.INFINITE
        animacionNube3.interpolator = LinearInterpolator()
        animacionNube3.start()

        // Esperar 5 segundos y abrir MainActivity
        nube1.postDelayed({

            animacionNube1.cancel()
            animacionNube2.cancel()
            animacionNube3.cancel()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()

        }, 5000)
    }
}