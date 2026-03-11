package com.example.oblig2_quizapp

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class QuizActivity :  {

    var viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

}