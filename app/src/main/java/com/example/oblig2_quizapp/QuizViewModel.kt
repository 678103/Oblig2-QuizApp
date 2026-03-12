package com.example.oblig2_quizapp

import androidx.lifecycle.ViewModel

public class QuizViewModel: ViewModel() {

    //make a galleryViewModel , list of animals pictures and names
    //newImage- gi den en state
    //no remember here since were not in a Composable

    //denne klassen holds all the data needed for the UI
    // brukes til å connecte til UI. Her skal alle bilder håndteres
 // Her må vi ha score for riktig poengscore
    // i tillegg må det inneholde spørsmålet

    var score = 0
    var attempt = 1

    //denne funksjonen vil øke scoren med 1 hvis den er rett
    fun increaseCount(){
        score++
    }
}