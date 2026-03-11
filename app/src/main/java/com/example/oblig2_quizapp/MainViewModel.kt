package com.example.oblig2_quizapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

public class MainViewModel: ViewModel() {

    //denne klassen holds all the data needed for the UI
    // brukes til å connecte til UI. Her skal alle bilder håndteres
 // Her må vi ha score for riktig poengscore

    var score = 0
    var attempt = 1

    //denne funksjonen vil øke scoren med 1 hvis den er rett
    fun addNumber(){
        score++
    }
}