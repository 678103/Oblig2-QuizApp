package com.example.oblig2_quizapp

import android.app.Application
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScoreTest {

    @get:Rule
    //starter ikke en hel activity, men kan styre selv hva som vises med setContent
    val composeTestRule = createComposeRule()

    //Deklarerer viewModel slik at det kan brukes i alle testene
    private lateinit var quizViewModel : QuizViewModel


    //kjøres automatisk før hver test
    @Before
    fun setup() {
        //henter en testversjon av application
        val application = ApplicationProvider.getApplicationContext<Application>()
        //henter en fersk  ViewModel til hver test
         quizViewModel = QuizViewModel(application)

        //Starter testen direkte med setContent
        composeTestRule.setContent {
            QuizScreen(quizViewModel) }
    }

    @Test
    fun score_startsAtZero() {
        //Tester at score begynner på null
        composeTestRule
            .onNodeWithTag("score_test")
            .assertTextEquals("Score: 0")
    }

    @Test
    fun score_incrementsOnRightAnswerClick(){
        //Klikker på riktig svar som er satt med hjelpemetode i QuizViewModel
        composeTestRule
            .onNodeWithText(quizViewModel.currentAnimal?.animalName ?: "")
            .performClick()

        composeTestRule
            .onNodeWithTag("score_test")
            .assertTextEquals("Score: 1")
    }

    @Test
    fun score_afterWrongAnswer(){

        //Lager et vilkårlig feil svar av answers for å sjekke at score blir null igjen
        var wrongAnswer = quizViewModel.answers
            .filterNot{ it.animalName == quizViewModel.currentAnimal?.animalName}
            .first()

        //Klikker på feil svar
        composeTestRule
            .onNodeWithText(wrongAnswer.animalName)
            .performClick()

        //Score viser null
        composeTestRule
            .onNodeWithTag("score_test")
            .assertTextEquals("Score: 0")
    }
    //her kan vi også lage en variabel for å gjøre det mindre hardkodet!
    //deretter teste random med random svar

}
