package com.example.oblig2_quizapp


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Rule
import org.junit.Test

class IntentTest {

    //setter opp JUnit-annotasjon med testregler
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>() //inngangspunkt for testen

@Test
fun clickQuizBtn_opensQuizActivity() {

        onView(withId(R.id.btnQuiz)) //gjennom espresso fordi xml med id
            .perform(click()) //simulerer et klikk på knappen

        composeRule.onNodeWithTag("quizApp_text")
        .assertIsDisplayed()
        .assertTextEquals("QuizApp!")

}
}