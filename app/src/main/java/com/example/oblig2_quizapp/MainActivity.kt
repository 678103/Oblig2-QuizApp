
package com.example.oblig2_quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //onCreate

        setContentView(R.layout.activity_main) //her deklarerer vi UI fra xml

        //finner så knappene vi ønsker å lage intent med
        val btnGallery: Button = findViewById<Button>(R.id.btnGallery)
        val btnQuiz: Button = findViewById<Button>(R.id.btnQuiz)

        //btnGallery, legger til en lytter som er onClick
        // View.onClicklistener er et interface
        btnGallery.setOnClickListener {
            //   startActivity(Intent(this, GalleryActivity::class.java))
            val intentGallery = Intent(this, GalleryActivity::class.java)
            startActivity(intentGallery)
        }

//        btnQuiz.setOnClickListener {
//            val intentQuiz = Intent(this, QuizActivity::class.java)
//            startActivity(intentQuiz) //vi trigger nå ny activity. intent er som en slags mld
//        }
    }
}
